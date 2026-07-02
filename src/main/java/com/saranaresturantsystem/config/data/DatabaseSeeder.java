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
        PermissionGroup User = getOrCreateGroup("Users", "Users", "Users");
        PermissionGroup Permission = getOrCreateGroup("Permission", "Permission", "Permissions related to Permission operations");
        PermissionGroup Role = getOrCreateGroup("Role", "Role", "Permissions related to Role operations");
        PermissionGroup PermissionGroup = getOrCreateGroup("PermissionGroup", "PermissionGroup", "Permissions related to PermissionGroup operations");
        
        // finances 
        PermissionGroup Bank = getOrCreateGroup("Bank", "Bank", "Permissions related to bank operations");
        PermissionGroup Currency = getOrCreateGroup("Currency", "Currency", "Permissions related to currency operations");

        // inventory
        PermissionGroup Adjustment = getOrCreateGroup("Adjustment", "Adjustment", "Permissions related to adjustment operations");
        PermissionGroup Stock = getOrCreateGroup("Stock", "Stock", "Permissions related to stock operations");
        PermissionGroup Store = getOrCreateGroup("Store", "Store", "Permissions related to store operations");
        PermissionGroup Transfer = getOrCreateGroup("Transfer", "Transfer", "Permissions related to transfer operations");
        
        // product
        PermissionGroup Product = getOrCreateGroup("Product", "Product", "Permissions related to product operations");
        PermissionGroup Category = getOrCreateGroup("Category", "Category", "Permissions related to category operations");
        PermissionGroup SubCategory = getOrCreateGroup("SubCategory", "SubCategory", "Permission related to SubCategory operation");
        PermissionGroup Unit = getOrCreateGroup("Unit", "Unit", "Permission related to Unit operation");
        

        // purchase
        PermissionGroup Purchase = getOrCreateGroup("Purchase", "Purchase", "Permissions related to purchase operations");
        PermissionGroup ExpenseType = getOrCreateGroup("ExpenseType", "ExpenseType", "Permissions related to expense type operations");
        PermissionGroup OrderItem = getOrCreateGroup("OrderItem", "OrderItem", "Permissions related to order item operations");
        // PermissionGroup Order = getOrCreateGroup("Order", "Order", "Permissions related to order operations");
        PermissionGroup Supplier = getOrCreateGroup("Supplier", "Supplier", "Permissions related to supplier operations");
        
        // report
        // PermissionGroup Report = getOrCreateGroup("Report", "Report", "Permissions related to report operations");
        
        // sale
        PermissionGroup Sale = getOrCreateGroup("Sale", "Sale", "Permissions related to sale operations");
        PermissionGroup Group = getOrCreateGroup("Group", "Group", "Permissions related to group operations");
        PermissionGroup Table = getOrCreateGroup("Table", "Table", "Permissions related to table operations");
        PermissionGroup Sellers = getOrCreateGroup("Sellers", "Sellers", "Permissions related to sellers operations");

        // // report
        // PermissionGroup ExpenseReport = getOrCreateGroup("ExpenseReport", "ExpenseReport", "Permissions related to expense report operations");
        // PermissionGroup StockTaking = getOrCreateGroup("StockTaking", "StockTaking", "Permissions related to stock taking operations");
        // PermissionGroup LowStockReport = getOrCreateGroup("LowStockReport", "LowStockReport", "Permissions related to low stock report operations");
        // PermissionGroup ProductionReport = getOrCreateGroup("ProductionReport", "ProductionReport", "Permissions related to production report operations");
        // PermissionGroup SaleReport = getOrCreateGroup("SalesReport", "SalesReport", "Permissions related to sale report operations");
        // PermissionGroup PurchaseReport = getOrCreateGroup("PurchaseReport", "PurchaseReport", "Permissions related to purchase report operations");
        // PermissionGroup ProfitAndLossReport = getOrCreateGroup("ProfitAndLossReport", "ProfitAndLossReport", "Permissions related to profit and loss report operations");
        // PermissionGroup CustomerSupplierReport = getOrCreateGroup("CustomerSupplierReport", "CustomerSupplierReport", "Permissions related to customer and supplier report operations");
        // PermissionGroup EmployeeReport = getOrCreateGroup("EmployeeReport", "EmployeeReport", "Permissions related to employee report operations");
        // PermissionGroup StockMovementReport = getOrCreateGroup("StockMovementReport", "StockMovementReport", "Permissions related to stock movement report operations");
        // PermissionGroup InventorySummary = getOrCreateGroup("InventorySummary", "InventorySummary", "Permissions related to inventory summary report operations");

        
        // 3. Create all permissions and collect them
        Set<Permission> allPermissions = new HashSet<>();

        // Finances
        // ========== bank ==========================
        allPermissions.add(getOrCreatePermission("bank:read", "Read Banks", "Ability to view banks list and details",   Bank));
        allPermissions.add(getOrCreatePermission("bank:create", "Create Banks", "Ability to create new banks", Bank));
        allPermissions.add(getOrCreatePermission("bank:update", "Update Banks", "Ability to update existing banks", Bank));
        allPermissions.add(getOrCreatePermission("bank:delete", "Delete Banks", "Ability to delete banks", Bank));

        //================ currency ============================
        allPermissions.add(getOrCreatePermission("currency:read", "Read Currencies", "Ability to view currencies list and details", Currency));
        allPermissions.add(getOrCreatePermission("currency:create", "Create Currencies", "Ability to create new currencies", Currency));
        allPermissions.add(getOrCreatePermission("currency:update", "Update Currencies", "Ability to update existing currencies", Currency));
        allPermissions.add(getOrCreatePermission("currency:delete", "Delete Currencies", "Ability to delete currencies", Currency));

        // Inventory
        // ====================== adjustment =====================
        allPermissions.add(getOrCreatePermission("adjustment:read", "Read Adjustments", "Ability to view stock adjustments", Adjustment));
        allPermissions.add(getOrCreatePermission("adjustment:create", "Create Adjustments", "Ability to create stock adjustments", Adjustment));
        allPermissions.add(getOrCreatePermission("adjustment:update", "Update Adjustments", "Ability to update stock adjustments", Adjustment));
        allPermissions.add(getOrCreatePermission("adjustment:delete", "Delete Adjustments", "Ability to delete stock adjustments", Adjustment));

        // =================== stock ==============================
        allPermissions.add(getOrCreatePermission("stock:read", "Read Stocks", "Ability to view stock levels", Stock));
        allPermissions.add(getOrCreatePermission("stock:update", "Update Stocks", "Ability to update stock levels", Stock));

        // =================== store ==============================
        allPermissions.add(getOrCreatePermission("store:read", "Read Stores", "Ability to view stores", Store));
        allPermissions.add(getOrCreatePermission("store:create", "Create Stores", "Ability to create stores", Store));
        allPermissions.add(getOrCreatePermission("store:update", "Update Stores", "Ability to update stores", Store));
        allPermissions.add(getOrCreatePermission("store:delete", "Delete Stores", "Ability to delete stores", Store));

        // =================== transfer =============================
        allPermissions.add(getOrCreatePermission("transfer:read", "Read Transfers", "Ability to view store transfers", Transfer));
        allPermissions.add(getOrCreatePermission("transfer:create", "Create Transfers", "Ability to create store transfers", Transfer));
        allPermissions.add(getOrCreatePermission("transfer:update", "Update Transfers", "Ability to update store transfers", Transfer));
        allPermissions.add(getOrCreatePermission("transfer:delete", "Delete Transfers", "Ability to delete store transfers", Transfer));
        allPermissions.add(getOrCreatePermission("transfer:approve", "Approve Transfers", "Ability to approve store transfers", Transfer));
        allPermissions.add(getOrCreatePermission("transfer:completed", "Complete Transfers", "Ability to complete store transfers", Transfer));
        allPermissions.add(getOrCreatePermission("transfer:cancel", "Cancel Transfers", "Ability to cancel store transfers", Transfer));

        // Products
        // ====================== category ============================
        allPermissions.add(getOrCreatePermission("category:read", "Read Categories", "Ability to view categories", Category));
        allPermissions.add(getOrCreatePermission("category:create", "Create Categories", "Ability to create new categories", Category));
        allPermissions.add(getOrCreatePermission("category:update", "Update Categories", "Ability to update existing categories", Category));
        allPermissions.add(getOrCreatePermission("category:delete", "Delete Categories", "Ability to delete categories", Category));

        // ====================== product ===========================
        allPermissions.add(getOrCreatePermission("product:read", "Read Products", "Ability to view products", Product));
        allPermissions.add(getOrCreatePermission("product:create", "Create Products", "Ability to create new products", Product));
        allPermissions.add(getOrCreatePermission("product:update", "Update Products", "Ability to update existing products", Product));
        allPermissions.add(getOrCreatePermission("product:delete", "Delete Products", "Ability to delete products", Product));

        // ====================== subCategory ========================
        allPermissions.add(getOrCreatePermission("subCategory:read", "Read Sub-categories", "Ability to view sub-categories", SubCategory));
        allPermissions.add(getOrCreatePermission("subCategory:create", "Create Sub-categories", "Ability to create new sub-categories", SubCategory));
        allPermissions.add(getOrCreatePermission("subCategory:update", "Update Sub-categories", "Ability to update existing sub-categories", SubCategory));
        allPermissions.add(getOrCreatePermission("subCategory:delete", "Delete Sub-categories", "Ability to delete sub-categories", SubCategory));

        // ====================== unit ==============================
        allPermissions.add(getOrCreatePermission("unit:read", "Read Units", "Ability to view units", Unit));
        allPermissions.add(getOrCreatePermission("unit:create", "Create Units", "Ability to create units", Unit));
        allPermissions.add(getOrCreatePermission("unit:update", "Update Units", "Ability to update units", Unit));
        allPermissions.add(getOrCreatePermission("unit:delete", "Delete Units", "Ability to delete units", Unit));

        // Purchases
        // ====================== expensesType =========================
        allPermissions.add(getOrCreatePermission("expensesType:read", "Read Expenses Types", "Ability to view expenses types", ExpenseType));
        allPermissions.add(getOrCreatePermission("expensesType:create", "Create Expenses Types", "Ability to create new expenses types", ExpenseType));
        allPermissions.add(getOrCreatePermission("expensesType:update", "Update Expenses Types", "Ability to update existing expenses types", ExpenseType));
        allPermissions.add(getOrCreatePermission("expensesType:delete", "Delete Expenses Types", "Ability to delete expenses types", ExpenseType));

        // ====================== orderItem ===========================
        allPermissions.add(getOrCreatePermission("orderItem:read", "Read Order Items", "Ability to view order items", OrderItem));
        allPermissions.add(getOrCreatePermission("orderItem:create", "Create Order Items", "Ability to create new order items", OrderItem));
        allPermissions.add(getOrCreatePermission("orderItem:update", "Update Order Items", "Ability to update existing order items", OrderItem));
        allPermissions.add(getOrCreatePermission("orderItem:delete", "Delete Order Items", "Ability to delete order items", OrderItem));

        // ====================== purchases ===========================
        allPermissions.add(getOrCreatePermission("purchase:read", "Read Purchases", "Ability to view purchase orders", Purchase));
        allPermissions.add(getOrCreatePermission("purchase:create", "Create Purchases", "Ability to create purchase orders", Purchase));
        allPermissions.add(getOrCreatePermission("purchase:update", "Update Purchases", "Ability to update purchase orders", Purchase));
        allPermissions.add(getOrCreatePermission("purchase:delete", "Delete Purchases", "Ability to delete purchase orders", Purchase));
        allPermissions.add(getOrCreatePermission("purchase:approve", "Approve Purchases", "Ability to approve purchase orders", Purchase));
        allPermissions.add(getOrCreatePermission("purchase:completed", "Complete Purchases", "Ability to complete purchase orders", Purchase));
        allPermissions.add(getOrCreatePermission("purchase:cancel", "Cancel Purchases", "Ability to cancel purchase orders", Purchase));
        // ======================= supplier =============================
        allPermissions.add(getOrCreatePermission("supplier:read", "Read Suppliers", "Ability to view suppliers", Supplier));
        allPermissions.add(getOrCreatePermission("supplier:create", "Create Suppliers", "Ability to create new suppliers", Supplier));
        allPermissions.add(getOrCreatePermission("supplier:update", "Update Suppliers", "Ability to update existing suppliers", Supplier));
        allPermissions.add(getOrCreatePermission("supplier:delete", "Delete Suppliers", "Ability to delete suppliers", Supplier));

        // Reports

        // Sales
        //  ====================== Group ====================
        allPermissions.add(getOrCreatePermission("group:read", "Read Groups", "Ability to view groups", Group));
        allPermissions.add(getOrCreatePermission("group:create", "Create Groups", "Ability to create new groups", Group));
        allPermissions.add(getOrCreatePermission("group:update", "Update Groups", "Ability to update existing groups", Group));
        allPermissions.add(getOrCreatePermission("group:delete", "Delete Groups", "Ability to delete groups", Group));


        /// ====================== option =========================
        allPermissions.add(getOrCreatePermission("option:read", "Read Options", "Ability to view options", Group));
        allPermissions.add(getOrCreatePermission("option:create", "Create Options", "Ability to create new options", Group));
        allPermissions.add(getOrCreatePermission("option:update", "Update Options", "Ability to update existing options", Group));
        allPermissions.add(getOrCreatePermission("option:delete", "Delete Options", "Ability to delete options", Group));

        // ======================== sale ==============================
        allPermissions.add(getOrCreatePermission("sale:read", "Read Sales", "Ability to view sales transactions", Sale));
        allPermissions.add(getOrCreatePermission("sale:create", "Create Sales", "Ability to create sales transactions", Sale));
        allPermissions.add(getOrCreatePermission("sale:update", "Update Sales", "Ability to update sales transactions", Sale));
        allPermissions.add(getOrCreatePermission("sale:delete", "Delete Sales", "Ability to delete sales transactions", Sale));
        allPermissions.add(getOrCreatePermission("sale:read", "Read Reports", "Ability to view sales and system reports", Sale));

        // ===================== seller ================================
        allPermissions.add(getOrCreatePermission("seller:read", "Read Sellers", "Ability to view sellers", Sellers));
        allPermissions.add(getOrCreatePermission("seller:create", "Create Sellers", "Ability to create sellers", Sellers));
        allPermissions.add(getOrCreatePermission("seller:update", "Update Sellers", "Ability to update sellers", Sellers));
        allPermissions.add(getOrCreatePermission("seller:delete", "Delete Sellers", "Ability to delete sellers", Sellers));

        // ======================= tables ===========================
        allPermissions.add(getOrCreatePermission("table:read", "Read Tables", "Ability to view tables", Table));
        allPermissions.add(getOrCreatePermission("table:create", "Create Tables", "Ability to create tables", Table));
        allPermissions.add(getOrCreatePermission("table:update", "Update Tables", "Ability to update tables", Table));
        allPermissions.add(getOrCreatePermission("table:delete", "Delete Tables", "Ability to delete tables", Table));

        // Users
        // ================ users ====================================
        allPermissions.add(getOrCreatePermission("user:read", "Read Users", "Ability to view users list and details", User));
        allPermissions.add(getOrCreatePermission("user:create", "Create Users", "Ability to create new users", User));
        allPermissions.add(getOrCreatePermission("user:update", "Update Users", "Ability to update existing users", User));
        allPermissions.add(getOrCreatePermission("user:delete", "Delete Users", "Ability to delete users", User));

        // ================= role ===============================
        allPermissions.add(getOrCreatePermission("role:read", "Read Roles", "Ability to view roles", Role));
        allPermissions.add(getOrCreatePermission("role:create", "Create Roles", "Ability to create new roles", Role));
        allPermissions.add(getOrCreatePermission("role:update", "Update Roles", "Ability to update existing roles", Role));
        allPermissions.add(getOrCreatePermission("role:delete", "Delete Roles", "Ability to delete roles", Role));

        // ======================== permission =====================
        allPermissions.add(getOrCreatePermission("permission:read", "Read Permissions", "Ability to view permissions", Permission));
        allPermissions.add(getOrCreatePermission("permission:create", "Create Permissions", "Ability to create new permissions", Permission));
        allPermissions.add(getOrCreatePermission("permission:update", "Update Permissions", "Ability to update existing permissions", Permission));
        allPermissions.add(getOrCreatePermission("permission:delete", "Delete Permissions", "Ability to delete permissions", Permission));

        // ========================== permission group ===============
        allPermissions.add(getOrCreatePermission("permissionGroup:read", "Read Permission Groups", "Ability to view permission groups", PermissionGroup));
        allPermissions.add(getOrCreatePermission("permissionGroup:create", "Create Permission Groups", "Ability to create permission groups", PermissionGroup));
        allPermissions.add(getOrCreatePermission("permissionGroup:update", "Update Permission Groups", "Ability to update permission groups", PermissionGroup));
        allPermissions.add(getOrCreatePermission("permissionGroup:delete", "Delete Permission Groups", "Ability to delete permission groups", PermissionGroup));


        // 4. Create or get "SUPER_ADMIN" Role
        Role SUPER_ADMIN = roleRepository.findByCode("SUPER_ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setCode("SUPER_ADMIN");
            role.setName("Super Administrator");
            role.setDescription("Super Administrator role with absolute database permissions");
            return roleRepository.save(role);
        });

        Role userRole = roleRepository.findByCode("USER").orElseGet(() -> {
            Role role = new Role();
            role.setCode("USER");
            role.setName("USER");
            role.setDescription("User role with basic permissions");
            return roleRepository.save(role);
        });

        // 5. Assign all permissions to "SUPER_ADMIN" role
        SUPER_ADMIN.setPermissions(allPermissions);
        roleRepository.save(SUPER_ADMIN);
        log.info("Assigned {} permissions to the 'supAdmin' role successfully.", allPermissions.size());

        // 6. Create or get "admin" Role
        Role ADMIN = roleRepository.findByCode("ADMIN").orElseGet(() -> {
            Role role = new Role();
            role.setCode("ADMIN");
            role.setName("Admin");
            role.setDescription("Admin staff role with administrative and operational permissions");
            return roleRepository.save(role);
        });


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
        log.info("Super Admin user 'namyou854@gmail.com' created and seeded successfully with role 'SUPER ADMIN'!");

        User user = new User();
        user.setFirstName("User");
        user.setLastName("User");
        user.setUsername("USER");
        user.setEmail("user@gmail.com");
        user.setPhone("012345679");
        user.setPasswordHash(passwordEncoder.encode("admin@123"));
        user.setIsActive(StatusType.ACTIVE);
        user.setIsVerified(true);
        user.setIsLocked(false);
        user.setFailedLoginAttempts(0);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(userRole);
        user.setRoles(userRoles);

        userRepository.save(user);
        log.info("User 'user@gmail.com' created and seeded successfully with role 'USER'!");

        // ===== CREATE ADMIN LOGIN ===========
        User admin = new User();
        admin.setFirstName("Admin");
        admin.setLastName("Admin");
        admin.setUsername("ADMIN");
        admin.setEmail("admin@gmail.com");
        admin.setPhone("012345680");
        admin.setPasswordHash(passwordEncoder.encode("admin@123"));
        admin.setIsActive(StatusType.ACTIVE);
        admin.setIsVerified(true);
        admin.setIsLocked(false);
        admin.setFailedLoginAttempts(0);
        admin.setCreatedAt(LocalDateTime.now());
        admin.setUpdatedAt(LocalDateTime.now());
        Set<Role> adminRoles = new HashSet<>();
        adminRoles.add(ADMIN);
        admin.setRoles(adminRoles);
        userRepository.save(admin);
        log.info("Admin 'admin@gmail.com' created and seeded successfully with role 'ADMIN'!");

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
