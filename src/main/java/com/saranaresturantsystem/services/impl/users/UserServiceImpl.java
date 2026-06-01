package com.saranaresturantsystem.services.impl.users;

import com.saranaresturantsystem.common.UniqueChecker;
import com.saranaresturantsystem.dto.request.users.UserRequest;
import com.saranaresturantsystem.dto.response.users.UserResponse;
import com.saranaresturantsystem.entities.inventory.Store;
import com.saranaresturantsystem.entities.users.Role;
import com.saranaresturantsystem.entities.users.User;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.execption.DuplicateResourceException;
import com.saranaresturantsystem.execption.ResourceNotFoundException;
import com.saranaresturantsystem.mappers.users.UserMapper;
import com.saranaresturantsystem.repositories.users.RoleRepository;
import com.saranaresturantsystem.repositories.inventory.StoreRepository;
import com.saranaresturantsystem.repositories.users.UserRepository;
import com.saranaresturantsystem.services.users.UserService;
import com.saranaresturantsystem.utils.PageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private  final UserMapper userMapper ;
    private  final UniqueChecker uniqueChecker  ;
    @Override
    public Page<UserResponse> getAll(Map<String, String> params) {
        User currentUser = getCurrentUser();
       Pageable pageable = PageUtil.fromParams(params);
        Page<User> usersPage;
        if (currentUser.getStore() != null) {
            boolean isSuperAdmin = hasRole(currentUser, "superAdmin");
            if (!isSuperAdmin) {
                usersPage = userRepository.findByStoreIdAndDeletedAtIsNull(currentUser.getStore().getId(), pageable);
            } else {
                usersPage = userRepository.findByDeletedAtIsNull(pageable);
            }
        } else {
            usersPage = userRepository.findByDeletedAtIsNull(pageable);
        }
        return usersPage.map(userMapper::toResponse);
    }

    @Override
    public UserResponse getById(Long id) {
        User currentUser = getCurrentUser();
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));

        if (user.getDeletedAt() != null) {
            throw new ResourceNotFoundException("User", id);
        }

        if (currentUser.getStore() != null) {
            boolean isSuperAdmin = hasRole(currentUser, "SuperAdmin");
            if (!isSuperAdmin) {
                if (user.getStore() == null || !user.getStore().getId().equals(currentUser.getStore().getId())) {
                    throw new AccessDeniedException("You do not have permission to access this user.");
                }
            }
        }
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse create(UserRequest request) {
        User currentUser = getCurrentUser();
        Long targetStoreId = request.getStoreId();

        // Enforce store boundary for non-super-admins
        if (currentUser.getStore() != null) {
            boolean isSuperAdmin = hasRole(currentUser, "superAdmin");
            if (!isSuperAdmin) {
                if (targetStoreId == null || !targetStoreId.equals(currentUser.getStore().getId())) {
                    throw new AccessDeniedException("You can only create users for your own store.");
                }
            }
        }

        // Validate unique username & email
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
        throw  new  DuplicateResourceException("Username already exists");
        }
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new DuplicateResourceException("Email already exists");
        }
        User user = userMapper.toEntity(request);
        user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash() !=null ? user.getPasswordHash() : "12345"));
        user.setIsLocked(false);
        user.setIsVerified(true);
        if (targetStoreId != null) {
            Store store = storeRepository.findById(targetStoreId)
                    .orElseThrow(() -> new ResourceNotFoundException("Store", targetStoreId));
            user.setStore(store);
        }

        // Handle Role Assignments
        Set<Role> roles = new HashSet<>();
        if (request.getRoleCodes() != null && !request.getRoleCodes().isEmpty()) {
            for (String code : request.getRoleCodes()) {
                Role role = roleRepository.findByCode(code).orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + code));
                // Restrict store admin role assignments
                if (currentUser.getStore() != null && !hasRole(currentUser, "SuperAdmin")) {
                    if (!"ROLE_STAFF".equals(code) && !"ROLE_SALE".equals(code) && !"ROLE_MANAGER".equals(code)) {
                        throw new AccessDeniedException("Store Administrator can only assign ROLE_STAFF, ROLE_SALE, and ROLE_MANAGER roles.");}
                }
                roles.add(role);
            }
        }
        user.setRoles(roles);
        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }


    @Override
    @Transactional
    public UserResponse update(Long id, UserRequest request) {
        User currentUser = getCurrentUser();
        User user = findById(id);

        // Store boundary checks
        if (currentUser.getStore() != null) {
            boolean isSuperAdmin = hasRole(currentUser, "ROLE_SUPER_ADMIN");
            if (!isSuperAdmin) {
                if (user.getStore() == null || !user.getStore().getId().equals(currentUser.getStore().getId())) {
                    throw new AccessDeniedException("You do not have permission to manage this user.");
                }
                if (request.getStoreId() == null || !request.getStoreId().equals(currentUser.getStore().getId())) {
                    throw new AccessDeniedException("You cannot transfer users to other stores.");
                }
            }
        }

        uniqueChecker.verify(userRepository , user , "email" , request.getEmail());
        uniqueChecker.verify(userRepository , user , "userName" , request.getUsername());

        // Validate username/email duplication (if changed)
//        if (!user.getUsername().equals(request.getUsername())) {
//            userRepository.findByUsername(request.getUsername()).ifPresent(u -> {
//                throw  new   DuplicateResourceException("Username already exists");
//            });
//            user.setUsername(request.getUsername());
//        }
//        if (!user.getEmail().equals(request.getEmail())) {
//            userRepository.findByEmail(request.getEmail()).ifPresent(u -> {
//                throw new DuplicateResourceException("Email already exists");
//            });
//            user.setEmail(request.getEmail());
//        }

        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPasswordHash(passwordEncoder.encode(request.getPassword()));
        }

        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setPhone(request.getPhone());
        user.setIsActive(request.getIsActive() != null ? request.getIsActive() : user.getIsActive());

//        if (request.getStoreId() != null) {
//            Store store = storeRepository.findById(request.getStoreId())
//                    .orElseThrow(() -> new ResourceNotFoundException("Store", request.getStoreId()));
//            user.setStore(store);
//        } else {
//            user.setStore(null);
//        }

        // Role update checks
        if (request.getRoleCodes() != null) {
            Set<Role> roles = new java.util.HashSet<>();
            for (String code : request.getRoleCodes()) {
                Role role = roleRepository.findByCode(code)
                        .orElseThrow(() -> new ResourceNotFoundException("Role not found with code: " + code));

                if (currentUser.getStore() != null && !hasRole(currentUser, "ROLE_SUPER_ADMIN")) {
                    if (!"ROLE_STAFF".equals(code) && !"ROLE_SALE".equals(code) && !"ROLE_MANAGER".equals(code)) {
                        throw new AccessDeniedException(
                                "Store Administrator can only assign ROLE_STAFF, ROLE_SALE, and ROLE_MANAGER roles."
                        );
                    }
                }
                roles.add(role);
            }
            user.setRoles(roles);
        }

        user = userRepository.save(user);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        User currentUser = getCurrentUser();
        User user = findById(id);
        if (currentUser.getStore() != null) {
            boolean isSuperAdmin = hasRole(currentUser, "ROLE_SUPER_ADMIN");
            if (!isSuperAdmin) {
                if (user.getStore() == null || !user.getStore().getId().equals(currentUser.getStore().getId())) {
                    throw new AccessDeniedException("You do not have permission to delete this user.");
                }
            }
        }
        user.setDeletedAt(LocalDateTime.now());
        user.setIsActive(StatusType.INACTIVE);
        userRepository.save(user);
    }

    @Override
    public User findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User", id));
        if (user.getDeletedAt() != null) {
            throw new ResourceNotFoundException("User", id);
        }
        return  user;
    }

    private User getCurrentUser() {
        String usernameOrEmail = Objects.requireNonNull(SecurityContextHolder.getContext().getAuthentication()).getName();
        return userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("Authenticated user not found"));
    }

    private boolean hasRole(User user, String roleCode) {
        return user.getRoles().stream().anyMatch(r -> roleCode.equals(r.getCode()));
    }

//    private UserResponse toResponse(User user) {
//        return UserResponse.builder()
//                .id(user.getId())
//                .username(user.getUsername())
//                .email(user.getEmail())
//                .firstName(user.getFirstName())
//                .lastName(user.getLastName())
//                .phone(user.getPhone())
//                .isActive(user.getIsActive())
//                .isVerified(user.getIsVerified())
//                .isLocked(user.getIsLocked())
//                .storeId(user.getStore() != null ? user.getStore().getId() : null)
//                .storeName(user.getStore() != null ? user.getStore().getName() : "ALL STORES")
//                .roles(user.getRoles().stream().map(Role::getCode).toList())
//                .createdAt(user.getCreatedAt())
//                .updatedAt(user.getUpdatedAt())
//                .build();
//    }
}
