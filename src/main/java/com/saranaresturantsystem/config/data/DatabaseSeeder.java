package com.saranaresturantsystem.config.data;

import com.saranaresturantsystem.entities.users.Permission;
import com.saranaresturantsystem.entities.users.PermissionGroup;
import com.saranaresturantsystem.entities.users.Role;
import com.saranaresturantsystem.entities.users.User;
import com.saranaresturantsystem.enums.StatusType;
import com.saranaresturantsystem.repositories.users.PermissionGroupRepository;
import com.saranaresturantsystem.repositories.users.PermissionRepository;
import com.saranaresturantsystem.repositories.users.RoleRepository;
import com.saranaresturantsystem.repositories.users.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class DatabaseSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final PermissionGroupRepository permissionGroupRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("Starting database seeding...");

        // 1. Check if the Super Admin user already exists
        if (userRepository.findByEmail("namyou854@gmail.com").isPresent()) {
            log.info("Super Admin user already exists. Skipping database seeding.");
            return;
        }

        // 2. Define standard Permission Groups
        PermissionGroup userMgmt = getOrCreateGroup("user_management", "User Management", "Permissions related to user account operations");
        PermissionGroup roleMgmt = getOrCreateGroup("role_management", "Role Management", "Permissions related to roles configuration");
        PermissionGroup permMgmt = getOrCreateGroup("permission_management", "Permission Management", "Permissions related to access controls");
        PermissionGroup prodMgmt = getOrCreateGroup("product_management", "Product Management", "Permissions related to inventory products");
        PermissionGroup catMgmt = getOrCreateGroup("category_management", "Category Management", "Permissions related to product categories");
        PermissionGroup adjMgmt = getOrCreateGroup("adjustment_management", "Adjustment Management", "Permissions related to stock adjustments");
        PermissionGroup stockMgmt = getOrCreateGroup("stock_management", "Stock Management", "Permissions related to stock levels");
        PermissionGroup saleMgmt = getOrCreateGroup("sale_management", "Sale Management", "Permissions related to sales transactions");
        PermissionGroup purchaseMgmt = getOrCreateGroup("purchase_management", "Purchase Management", "Permissions related to purchase transactions");
        PermissionGroup transferMgmt = getOrCreateGroup("transfer_management", "Transfer Management", "Permissions related to store-to-store transfers");
        PermissionGroup peopleMgmt = getOrCreateGroup("people_management", "People Management", "Permissions related to customers/suppliers/sellers");
        PermissionGroup settingMgmt = getOrCreateGroup("setting_management", "Setting Management", "Permissions related to configuration settings");
        PermissionGroup storeMgmt = getOrCreateGroup("store_management", "Store Management", "Permissions related to stores");

        // 3. Create all permissions and collect them
        Set<Permission> allPermissions = new HashSet<>();

        // User Management
        allPermissions.add(getOrCreatePermission("user:read", "Read Users", "Ability to view users list and details", userMgmt));
        allPermissions.add(getOrCreatePermission("user:create", "Create Users", "Ability to create new users", userMgmt));
        allPermissions.add(getOrCreatePermission("user:update", "Update Users", "Ability to update existing users", userMgmt));
        allPermissions.add(getOrCreatePermission("user:delete", "Delete Users", "Ability to delete users", userMgmt));

        // Role Management
        allPermissions.add(getOrCreatePermission("role:read", "Read Roles", "Ability to view roles", roleMgmt));
        allPermissions.add(getOrCreatePermission("role:create", "Create Roles", "Ability to create new roles", roleMgmt));
        allPermissions.add(getOrCreatePermission("role:update", "Update Roles", "Ability to update existing roles", roleMgmt));
        allPermissions.add(getOrCreatePermission("role:delete", "Delete Roles", "Ability to delete roles", roleMgmt));

        // Permission Management
        allPermissions.add(getOrCreatePermission("permission:read", "Read Permissions", "Ability to view permissions", permMgmt));
        allPermissions.add(getOrCreatePermission("permission:create", "Create Permissions", "Ability to create new permissions", permMgmt));
        allPermissions.add(getOrCreatePermission("permission:update", "Update Permissions", "Ability to update existing permissions", permMgmt));
        allPermissions.add(getOrCreatePermission("permission:delete", "Delete Permissions", "Ability to delete permissions", permMgmt));

        // Product Management
        allPermissions.add(getOrCreatePermission("product:read", "Read Products", "Ability to view products", prodMgmt));
        allPermissions.add(getOrCreatePermission("product:create", "Create Products", "Ability to create new products", prodMgmt));
        allPermissions.add(getOrCreatePermission("product:update", "Update Products", "Ability to update existing products", prodMgmt));
        allPermissions.add(getOrCreatePermission("product:delete", "Delete Products", "Ability to delete products", prodMgmt));

        // Category Management
        allPermissions.add(getOrCreatePermission("category:read", "Read Categories", "Ability to view categories", catMgmt));
        allPermissions.add(getOrCreatePermission("category:create", "Create Categories", "Ability to create new categories", catMgmt));
        allPermissions.add(getOrCreatePermission("category:update", "Update Categories", "Ability to update existing categories", catMgmt));
        allPermissions.add(getOrCreatePermission("category:delete", "Delete Categories", "Ability to delete categories", catMgmt));

        // Adjustment Management
        allPermissions.add(getOrCreatePermission("adjustment:read", "Read Adjustments", "Ability to view stock adjustments", adjMgmt));
        allPermissions.add(getOrCreatePermission("adjustment:create", "Create Adjustments", "Ability to create stock adjustments", adjMgmt));
        allPermissions.add(getOrCreatePermission("adjustment:update", "Update Adjustments", "Ability to update stock adjustments", adjMgmt));
        allPermissions.add(getOrCreatePermission("adjustment:delete", "Delete Adjustments", "Ability to delete stock adjustments", adjMgmt));

        // Stock Management
        allPermissions.add(getOrCreatePermission("stock:read", "Read Stocks", "Ability to view stock levels", stockMgmt));
        allPermissions.add(getOrCreatePermission("stock:update", "Update Stocks", "Ability to update stock levels", stockMgmt));

        // Sale Management
        allPermissions.add(getOrCreatePermission("sale:read", "Read Sales", "Ability to view sales orders", saleMgmt));
        allPermissions.add(getOrCreatePermission("sale:create", "Create Sales", "Ability to create sales orders", saleMgmt));
        allPermissions.add(getOrCreatePermission("sale:update", "Update Sales", "Ability to update sales orders", saleMgmt));
        allPermissions.add(getOrCreatePermission("sale:delete", "Delete Sales", "Ability to delete sales orders", saleMgmt));

        // Purchase Management
        allPermissions.add(getOrCreatePermission("purchase:read", "Read Purchases", "Ability to view purchase orders", purchaseMgmt));
        allPermissions.add(getOrCreatePermission("purchase:create", "Create Purchases", "Ability to create purchase orders", purchaseMgmt));
        allPermissions.add(getOrCreatePermission("purchase:update", "Update Purchases", "Ability to update purchase orders", purchaseMgmt));
        allPermissions.add(getOrCreatePermission("purchase:delete", "Delete Purchases", "Ability to delete purchase orders", purchaseMgmt));

        // Transfer Management
        allPermissions.add(getOrCreatePermission("transfer:read", "Read Transfers", "Ability to view store transfers", transferMgmt));
        allPermissions.add(getOrCreatePermission("transfer:create", "Create Transfers", "Ability to create store transfers", transferMgmt));
        allPermissions.add(getOrCreatePermission("transfer:update", "Update Transfers", "Ability to update store transfers", transferMgmt));
        allPermissions.add(getOrCreatePermission("transfer:delete", "Delete Transfers", "Ability to delete store transfers", transferMgmt));

        // People Management
        allPermissions.add(getOrCreatePermission("people:read", "Read People", "Ability to view customers/suppliers/sellers", peopleMgmt));
        allPermissions.add(getOrCreatePermission("people:create", "Create People", "Ability to create customers/suppliers/sellers", peopleMgmt));
        allPermissions.add(getOrCreatePermission("people:update", "Update People", "Ability to update customers/suppliers/sellers", peopleMgmt));
        allPermissions.add(getOrCreatePermission("people:delete", "Delete People", "Ability to delete customers/suppliers/sellers", peopleMgmt));

        // Setting Management
        allPermissions.add(getOrCreatePermission("setting:read", "Read Settings", "Ability to view configuration settings", settingMgmt));
        allPermissions.add(getOrCreatePermission("setting:create", "Create Settings", "Ability to create configurations", settingMgmt));
        allPermissions.add(getOrCreatePermission("setting:update", "Update Settings", "Ability to update configurations", settingMgmt));
        allPermissions.add(getOrCreatePermission("setting:delete", "Delete Settings", "Ability to delete configurations", settingMgmt));

        // Store Management
        allPermissions.add(getOrCreatePermission("store:read", "Read Stores", "Ability to view stores", storeMgmt));
        allPermissions.add(getOrCreatePermission("store:create", "Create Stores", "Ability to create stores", storeMgmt));
        allPermissions.add(getOrCreatePermission("store:update", "Update Stores", "Ability to update stores", storeMgmt));
        allPermissions.add(getOrCreatePermission("store:delete", "Delete Stores", "Ability to delete stores", storeMgmt));

        // 4. Create or get "supAdmin" Role
        Role superAdminRole = roleRepository.findByCode("supAdmin").orElseGet(() -> {
            Role role = new Role();
            role.setCode("supAdmin");
            role.setName("Super Administrator");
            role.setDescription("Super Administrator role with absolute database permissions");
            return roleRepository.save(role);
        });

        // 5. Assign all permissions to "supAdmin" role
        superAdminRole.setPermissions(allPermissions);
        roleRepository.save(superAdminRole);
        log.info("Assigned {} permissions to the 'supAdmin' role successfully.", allPermissions.size());

        // 6. Create or get "admin" Role
        Role adminRole = roleRepository.findByCode("admin").orElseGet(() -> {
            Role role = new Role();
            role.setCode("admin");
            role.setName("Admin");
            role.setDescription("Admin staff role with administrative and operational permissions");
            return roleRepository.save(role);
        });
        Set<Permission> adminStaffPermissions = allPermissions.stream()
                .filter(p -> !p.getCode().startsWith("user:") && !p.getCode().startsWith("role:") && !p.getCode().startsWith("permission:"))
                .collect(Collectors.toSet());
        allPermissions.stream()
                .filter(p -> p.getCode().equals("user:read") || p.getCode().equals("role:read") || p.getCode().equals("permission:read"))
                .forEach(adminStaffPermissions::add);
        adminRole.setPermissions(adminStaffPermissions);
        roleRepository.save(adminRole);
        log.info("Assigned {} permissions to the 'admin' role.", adminStaffPermissions.size());

        // 7. Create or get "stock" Role
        Role stockRole = roleRepository.findByCode("stock").orElseGet(() -> {
            Role role = new Role();
            role.setCode("stock");
            role.setName("Stock Staff");
            role.setDescription("Stock staff role with inventory and stock management permissions");
            return roleRepository.save(role);
        });
        Set<Permission> stockPermissions = allPermissions.stream()
                .filter(p -> p.getCode().startsWith("stock:") || p.getCode().startsWith("product:") || p.getCode().startsWith("category:") || p.getCode().startsWith("adjustment:") || p.getCode().startsWith("transfer:"))
                .collect(Collectors.toSet());
        stockRole.setPermissions(stockPermissions);
        roleRepository.save(stockRole);
        log.info("Assigned {} permissions to the 'stock' role.", stockPermissions.size());

        // 8. Create or get "cashier" Role
        Role cashierRole = roleRepository.findByCode("cashier").orElseGet(() -> {
            Role role = new Role();
            role.setCode("cashier");
            role.setName("Cashier Staff");
            role.setDescription("Cashier staff role with sales and cashier permissions");
            return roleRepository.save(role);
        });
        Set<Permission> cashierPermissions = allPermissions.stream()
                .filter(p -> p.getCode().startsWith("sale:") || p.getCode().startsWith("product:read") || p.getCode().startsWith("category:read") || p.getCode().startsWith("people:"))
                .collect(Collectors.toSet());
        cashierRole.setPermissions(cashierPermissions);
        roleRepository.save(cashierRole);
        log.info("Assigned {} permissions to the 'cashier' role.", cashierPermissions.size());

        // 9. Create or get "staff" Role (General Staff)
        Role staffRole = roleRepository.findByCode("staff").orElseGet(() -> {
            Role role = new Role();
            role.setCode("staff");
            role.setName("General Staff");
            role.setDescription("General staff role combining stock management and cashiering features");
            return roleRepository.save(role);
        });
        Set<Permission> staffPermissions = new HashSet<>();
        staffPermissions.addAll(stockPermissions);
        staffPermissions.addAll(cashierPermissions);
        staffRole.setPermissions(staffPermissions);
        roleRepository.save(staffRole);
        log.info("Assigned {} permissions to the 'staff' role (combining stock and cashier).", staffPermissions.size());

        // 10. Create Super Admin user
        User superAdmin = new User();
        superAdmin.setFirstName("Super");
        superAdmin.setLastName("Admin");
        superAdmin.setUsername("supadmin");
        superAdmin.setEmail("namyou854@gmail.com");
        superAdmin.setPhone("012345678");
        superAdmin.setPasswordHash(passwordEncoder.encode("admin@123"));
        superAdmin.setIsActive(StatusType.ACTIVE);
        superAdmin.setIsVerified(true);
        superAdmin.setIsLocked(false);
        superAdmin.setFailedLoginAttempts(0);
        superAdmin.setCreatedAt(LocalDateTime.now());
        superAdmin.setUpdatedAt(LocalDateTime.now());

        Set<Role> roles = new HashSet<>();
        roles.add(superAdminRole);
        superAdmin.setRoles(roles);

        userRepository.save(superAdmin);
        log.info("Super Admin user 'namyou854@gmail.com' created and seeded successfully with role 'supAdmin'!");
    }

    private PermissionGroup getOrCreateGroup(String code, String name, String description) {
        return permissionGroupRepository.findByCode(code).orElseGet(() -> {
            PermissionGroup group = new PermissionGroup();
            group.setCode(code);
            group.setName(name);
            group.setDescription(description);
            return permissionGroupRepository.save(group);
        });
    }

    private Permission getOrCreatePermission(String code, String name, String description, PermissionGroup group) {
        return permissionRepository.findByCode(code).orElseGet(() -> {
            Permission perm = new Permission();
            perm.setCode(code);
            perm.setName(name);
            perm.setDescription(description);
            perm.setGroup(group);
            return permissionRepository.save(perm);
        });
    }
}
