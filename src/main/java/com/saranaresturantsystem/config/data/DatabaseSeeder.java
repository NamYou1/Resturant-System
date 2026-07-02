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

        // 2. Define standard Permission Groups (following folder of controller)
        PermissionGroup financesGrp = getOrCreateGroup("finances", "Finances", "Permissions related to finance and banking operations");
        PermissionGroup inventoryGrp = getOrCreateGroup("inventory", "Inventory", "Permissions related to inventory operations");
        PermissionGroup productsGrp = getOrCreateGroup("products", "Products", "Permissions related to product operations");
        PermissionGroup purchasesGrp = getOrCreateGroup("purchases", "Purchases", "Permissions related to purchases operations");
        PermissionGroup reportsGrp = getOrCreateGroup("reports", "Reports", "Permissions related to reports");
        PermissionGroup salesGrp = getOrCreateGroup("sales", "Sales", "Permissions related to sales operations");
        PermissionGroup usersGrp = getOrCreateGroup("users", "Users", "Permissions related to user and access control operations");

        // 3. Create all permissions and collect them
        Set<Permission> allPermissions = new HashSet<>();

        // Finances
        // ========== bank ==========================
        allPermissions.add(getOrCreatePermission("bank:read", "Read Banks", "Ability to view banks list and details", financesGrp));
        allPermissions.add(getOrCreatePermission("bank:create", "Create Banks", "Ability to create new banks", financesGrp));
        allPermissions.add(getOrCreatePermission("bank:update", "Update Banks", "Ability to update existing banks", financesGrp));
        allPermissions.add(getOrCreatePermission("bank:delete", "Delete Banks", "Ability to delete banks", financesGrp));

        //================ currency ============================
        allPermissions.add(getOrCreatePermission("currency:read", "Read Currencies", "Ability to view currencies list and details", financesGrp));
        allPermissions.add(getOrCreatePermission("currency:create", "Create Currencies", "Ability to create new currencies", financesGrp));
        allPermissions.add(getOrCreatePermission("currency:update", "Update Currencies", "Ability to update existing currencies", financesGrp));
        allPermissions.add(getOrCreatePermission("currency:delete", "Delete Currencies", "Ability to delete currencies", financesGrp));

        // Inventory
        // ====================== adjustment =====================
        allPermissions.add(getOrCreatePermission("adjustment:read", "Read Adjustments", "Ability to view stock adjustments", inventoryGrp));
        allPermissions.add(getOrCreatePermission("adjustment:create", "Create Adjustments", "Ability to create stock adjustments", inventoryGrp));
        allPermissions.add(getOrCreatePermission("adjustment:update", "Update Adjustments", "Ability to update stock adjustments", inventoryGrp));
        allPermissions.add(getOrCreatePermission("adjustment:delete", "Delete Adjustments", "Ability to delete stock adjustments", inventoryGrp));

        // =================== stock ==============================
        allPermissions.add(getOrCreatePermission("stock:read", "Read Stocks", "Ability to view stock levels", inventoryGrp));
        allPermissions.add(getOrCreatePermission("stock:update", "Update Stocks", "Ability to update stock levels", inventoryGrp));

        // =================== store ==============================
        allPermissions.add(getOrCreatePermission("store:read", "Read Stores", "Ability to view stores", inventoryGrp));
        allPermissions.add(getOrCreatePermission("store:create", "Create Stores", "Ability to create stores", inventoryGrp));
        allPermissions.add(getOrCreatePermission("store:update", "Update Stores", "Ability to update stores", inventoryGrp));
        allPermissions.add(getOrCreatePermission("store:delete", "Delete Stores", "Ability to delete stores", inventoryGrp));

        // =================== transfer =============================
        allPermissions.add(getOrCreatePermission("transfer:read", "Read Transfers", "Ability to view store transfers", inventoryGrp));
        allPermissions.add(getOrCreatePermission("transfer:create", "Create Transfers", "Ability to create store transfers", inventoryGrp));
        allPermissions.add(getOrCreatePermission("transfer:update", "Update Transfers", "Ability to update store transfers", inventoryGrp));
        allPermissions.add(getOrCreatePermission("transfer:delete", "Delete Transfers", "Ability to delete store transfers", inventoryGrp));
        allPermissions.add(getOrCreatePermission("transfer:approve", "Approve Transfers", "Ability to approve store transfers", inventoryGrp));
        allPermissions.add(getOrCreatePermission("transfer:completed", "Complete Transfers", "Ability to complete store transfers", inventoryGrp));
        allPermissions.add(getOrCreatePermission("transfer:cancel", "Cancel Transfers", "Ability to cancel store transfers", inventoryGrp));

        // Products
        // ====================== category ============================
        allPermissions.add(getOrCreatePermission("category:read", "Read Categories", "Ability to view categories", productsGrp));
        allPermissions.add(getOrCreatePermission("category:create", "Create Categories", "Ability to create new categories", productsGrp));
        allPermissions.add(getOrCreatePermission("category:update", "Update Categories", "Ability to update existing categories", productsGrp));
        allPermissions.add(getOrCreatePermission("category:delete", "Delete Categories", "Ability to delete categories", productsGrp));

        // ====================== product ===========================
        allPermissions.add(getOrCreatePermission("product:read", "Read Products", "Ability to view products", productsGrp));
        allPermissions.add(getOrCreatePermission("product:create", "Create Products", "Ability to create new products", productsGrp));
        allPermissions.add(getOrCreatePermission("product:update", "Update Products", "Ability to update existing products", productsGrp));
        allPermissions.add(getOrCreatePermission("product:delete", "Delete Products", "Ability to delete products", productsGrp));

        // ====================== subCategory ========================
        allPermissions.add(getOrCreatePermission("subCategory:read", "Read Sub-categories", "Ability to view sub-categories", productsGrp));
        allPermissions.add(getOrCreatePermission("subCategory:create", "Create Sub-categories", "Ability to create new sub-categories", productsGrp));
        allPermissions.add(getOrCreatePermission("subCategory:update", "Update Sub-categories", "Ability to update existing sub-categories", productsGrp));
        allPermissions.add(getOrCreatePermission("subCategory:delete", "Delete Sub-categories", "Ability to delete sub-categories", productsGrp));

        // ====================== unit ==============================
        allPermissions.add(getOrCreatePermission("unit:read", "Read Units", "Ability to view units", productsGrp));
        allPermissions.add(getOrCreatePermission("unit:create", "Create Units", "Ability to create units", productsGrp));
        allPermissions.add(getOrCreatePermission("unit:update", "Update Units", "Ability to update units", productsGrp));
        allPermissions.add(getOrCreatePermission("unit:delete", "Delete Units", "Ability to delete units", productsGrp));

        // Purchases
        // ====================== expensesType =========================
        allPermissions.add(getOrCreatePermission("expensesType:read", "Read Expenses Types", "Ability to view expenses types", purchasesGrp));
        allPermissions.add(getOrCreatePermission("expensesType:create", "Create Expenses Types", "Ability to create new expenses types", purchasesGrp));
        allPermissions.add(getOrCreatePermission("expensesType:update", "Update Expenses Types", "Ability to update existing expenses types", purchasesGrp));
        allPermissions.add(getOrCreatePermission("expensesType:delete", "Delete Expenses Types", "Ability to delete expenses types", purchasesGrp));

        // ====================== orderItem ===========================
        allPermissions.add(getOrCreatePermission("orderItem:read", "Read Order Items", "Ability to view order items", purchasesGrp));
        allPermissions.add(getOrCreatePermission("orderItem:create", "Create Order Items", "Ability to create new order items", purchasesGrp));
        allPermissions.add(getOrCreatePermission("orderItem:update", "Update Order Items", "Ability to update existing order items", purchasesGrp));
        allPermissions.add(getOrCreatePermission("orderItem:delete", "Delete Order Items", "Ability to delete order items", purchasesGrp));

        // ====================== purchases ===========================
        allPermissions.add(getOrCreatePermission("purchase:read", "Read Purchases", "Ability to view purchase orders", purchasesGrp));
        allPermissions.add(getOrCreatePermission("purchase:create", "Create Purchases", "Ability to create purchase orders", purchasesGrp));
        allPermissions.add(getOrCreatePermission("purchase:update", "Update Purchases", "Ability to update purchase orders", purchasesGrp));
        allPermissions.add(getOrCreatePermission("purchase:delete", "Delete Purchases", "Ability to delete purchase orders", purchasesGrp));
        allPermissions.add(getOrCreatePermission("purchase:approve", "Approve Purchases", "Ability to approve purchase orders", purchasesGrp));
        allPermissions.add(getOrCreatePermission("purchase:completed", "Complete Purchases", "Ability to complete purchase orders", purchasesGrp));
        allPermissions.add(getOrCreatePermission("purchase:cancel", "Cancel Purchases", "Ability to cancel purchase orders", purchasesGrp));
        // ======================= supplier =============================
        allPermissions.add(getOrCreatePermission("supplier:read", "Read Suppliers", "Ability to view suppliers", purchasesGrp));
        allPermissions.add(getOrCreatePermission("supplier:create", "Create Suppliers", "Ability to create new suppliers", purchasesGrp));
        allPermissions.add(getOrCreatePermission("supplier:update", "Update Suppliers", "Ability to update existing suppliers", purchasesGrp));
        allPermissions.add(getOrCreatePermission("supplier:delete", "Delete Suppliers", "Ability to delete suppliers", purchasesGrp));

        // Reports

        // Sales
        //  ====================== Group ====================
        allPermissions.add(getOrCreatePermission("group:read", "Read Groups", "Ability to view groups", salesGrp));
        allPermissions.add(getOrCreatePermission("group:create", "Create Groups", "Ability to create new groups", salesGrp));
        allPermissions.add(getOrCreatePermission("group:update", "Update Groups", "Ability to update existing groups", salesGrp));
        allPermissions.add(getOrCreatePermission("group:delete", "Delete Groups", "Ability to delete groups", salesGrp));
        allPermissions.add(getOrCreatePermission("sale:read", "Read Reports", "Ability to view sales and system reports", reportsGrp));

        /// ====================== option =========================
        allPermissions.add(getOrCreatePermission("option:read", "Read Options", "Ability to view options", salesGrp));
        allPermissions.add(getOrCreatePermission("option:create", "Create Options", "Ability to create new options", salesGrp));
        allPermissions.add(getOrCreatePermission("option:update", "Update Options", "Ability to update existing options", salesGrp));
        allPermissions.add(getOrCreatePermission("option:delete", "Delete Options", "Ability to delete options", salesGrp));

        // ======================== sale ==============================
        allPermissions.add(getOrCreatePermission("sale:read", "Read Sales", "Ability to view sales transactions", salesGrp));
        allPermissions.add(getOrCreatePermission("sale:create", "Create Sales", "Ability to create sales transactions", salesGrp));
        allPermissions.add(getOrCreatePermission("sale:update", "Update Sales", "Ability to update sales transactions", salesGrp));
        allPermissions.add(getOrCreatePermission("sale:delete", "Delete Sales", "Ability to delete sales transactions", salesGrp));

        // ===================== seller ================================
        allPermissions.add(getOrCreatePermission("seller:read", "Read Sellers", "Ability to view sellers", salesGrp));
        allPermissions.add(getOrCreatePermission("seller:create", "Create Sellers", "Ability to create sellers", salesGrp));
        allPermissions.add(getOrCreatePermission("seller:update", "Update Sellers", "Ability to update sellers", salesGrp));
        allPermissions.add(getOrCreatePermission("seller:delete", "Delete Sellers", "Ability to delete sellers", salesGrp));

        // ======================= tables ===========================
        allPermissions.add(getOrCreatePermission("table:read", "Read Tables", "Ability to view tables", salesGrp));
        allPermissions.add(getOrCreatePermission("table:create", "Create Tables", "Ability to create tables", salesGrp));
        allPermissions.add(getOrCreatePermission("table:update", "Update Tables", "Ability to update tables", salesGrp));
        allPermissions.add(getOrCreatePermission("table:delete", "Delete Tables", "Ability to delete tables", salesGrp));

        // Users
        // ================ users ====================================
        allPermissions.add(getOrCreatePermission("user:read", "Read Users", "Ability to view users list and details", usersGrp));
        allPermissions.add(getOrCreatePermission("user:create", "Create Users", "Ability to create new users", usersGrp));
        allPermissions.add(getOrCreatePermission("user:update", "Update Users", "Ability to update existing users", usersGrp));
        allPermissions.add(getOrCreatePermission("user:delete", "Delete Users", "Ability to delete users", usersGrp));

        // ================= role ===============================
        allPermissions.add(getOrCreatePermission("role:read", "Read Roles", "Ability to view roles", usersGrp));
        allPermissions.add(getOrCreatePermission("role:create", "Create Roles", "Ability to create new roles", usersGrp));
        allPermissions.add(getOrCreatePermission("role:update", "Update Roles", "Ability to update existing roles", usersGrp));
        allPermissions.add(getOrCreatePermission("role:delete", "Delete Roles", "Ability to delete roles", usersGrp));

        // ======================== permission =====================
        allPermissions.add(getOrCreatePermission("permission:read", "Read Permissions", "Ability to view permissions", usersGrp));
        allPermissions.add(getOrCreatePermission("permission:create", "Create Permissions", "Ability to create new permissions", usersGrp));
        allPermissions.add(getOrCreatePermission("permission:update", "Update Permissions", "Ability to update existing permissions", usersGrp));
        allPermissions.add(getOrCreatePermission("permission:delete", "Delete Permissions", "Ability to delete permissions", usersGrp));

        // ========================== permission group ===============
        allPermissions.add(getOrCreatePermission("permissionGroup:read", "Read Permission Groups", "Ability to view permission groups", usersGrp));
        allPermissions.add(getOrCreatePermission("permissionGroup:create", "Create Permission Groups", "Ability to create permission groups", usersGrp));
        allPermissions.add(getOrCreatePermission("permissionGroup:update", "Update Permission Groups", "Ability to update permission groups", usersGrp));
        allPermissions.add(getOrCreatePermission("permissionGroup:delete", "Delete Permission Groups", "Ability to delete permission groups", usersGrp));

        // 4. Create or get "SUPER_ADMIN" Role
        Role SUPER_ADMIN = roleRepository.findByCode("SUPER_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setCode("SUPER_ADMIN");
            role.setName("Super Administrator");
            role.setDescription("Super Administrator role with absolute database permissions");
            return roleRepository.save(role);
        });

        // 5. Assign all permissions to "SUPER_ADMIN" role
        SUPER_ADMIN.setPermissions(allPermissions);
        roleRepository.save(SUPER_ADMIN);
        log.info("Assigned {} permissions to the 'supAdmin' role successfully.", allPermissions.size());

        // 6. Create or get "admin" Role
        // Role ADMIN = roleRepository.findByCode("ADMIN").orElseGet(() -> {
        //     Role role = new Role();
        //     role.setCode("ADMIN");
        //     role.setName("Admin");
        //     role.setDescription("Admin staff role with administrative and operational permissions");
        //     return roleRepository.save(role);
        // });
        User superAdmin = new User();
        superAdmin.setFirstName("Super");
        superAdmin.setLastName("Admin");
        superAdmin.setUsername("SUPER_ADMIN");
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
        roles.add(SUPER_ADMIN);
        superAdmin.setRoles(roles);

        userRepository.save(superAdmin);
        log.info("Super Admin user 'namyou854@gmail.com' created and seeded successfully with role 'superAdmin'!");
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
