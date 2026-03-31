# 📊 Architecture Diagrams - Category Request/Response System

## 1️⃣ Request/Response Flow Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     CLIENT (Frontend/API)                        │
└─────────────┬───────────────────────────────────────────────────┘
              │
              │ HTTP POST /api/v1/categories
              │ Content-Type: application/json
              │
        ┌─────▼──────┐
        │ CategoryRequest JSON
        │ {
        │   "code": "PIZZA",
        │   "name": "Pizza Category",
        │   "imageUrl": "..."
        │ }
        └─────┬──────┘
              │
        ┌─────▼────────────────────────────────────┐
        │ CategoryController.createCategory()       │
        │ @PostMapping                             │
        │ @Valid @RequestBody CategoryRequest      │
        └─────┬────────────────────────────────────┘
              │
        ┌─────▼─────────────────────────────┐
        │ Validation (Spring validates)     │
        │ ✓ @NotBlank                      │
        │ ✓ @Size(2, 50)                   │
        │ ✓ @ValidImageUrl (custom)        │
        └─────┬──────────────┬──────────────┘
              │              │
         VALID│              │INVALID
              │              │
         ┌────▼──┐    ┌──────▼──────────────────┐
         │Service│    │GbobleExecptionHandler   │
         └────┬──┘    │catches                  │
              │       │MethodArgumentNotValid   │
         ┌────▼────────────────────────────────┐
         │CategoryMapper.toCategoryFromRequest()│
         │Convert DTO → Entity                 │
         └────┬───────────────────────────────┘
              │
         ┌────▼──────────────────────────┐
         │CategoryRepository.save(entity)│
         │Insert into database           │
         └────┬───────────────────────────┘
              │
         ┌────▼──────────────────────────┐
         │CategoryMapper.toCategoryResponse()
         │Convert Entity → Response DTO  │
         └────┬───────────────────────────┘
              │
         ┌────▼──────────────────────────┐
         │Build ApiResponse<CategoryResp>│
         │- succeess: true               │
         │- status: CREATED              │
         │- payload: CategoryResponse    │
         │- timestamp: ISO_DATETIME      │
         └────┬───────────────────────────┘
              │
         ┌────▼──────────────────────────┐
         │HTTP 201 Response JSON         │
         └────┬───────────────────────────┘
              │
        ┌─────▼──────────────────────────┐
        │         CLIENT Browser          │
        │  Receives & displays response   │
        └────────────────────────────────┘
```

---

## 2️⃣ Validation Flow Diagram

```
┌──────────────────┐
│ CategoryRequest  │
│   JSON Data      │
└────────┬─────────┘
         │
    ┌────▼────────────────────────────────┐
    │ Spring Validation Process            │
    │ (@Valid annotation triggered)        │
    └────┬───────────────────┬─────────────┘
         │                   │
    ┌────▼─────────┐     ┌───▼────────────┐
    │ Field Level  │     │ Custom Level   │
    │ Validation   │     │ Validation     │
    └────┬─────────┘     └───┬────────────┘
         │                   │
   ┌─────▼────────────┐  ┌────▼──────────────┐
   │ Built-in JSR380  │  │ @ValidImageUrl    │
   │                  │  │ ImageUrlValidator │
   │ @NotBlank ✓      │  │                   │
   │ @Size ✓          │  │ Validates:        │
   │ @PastOrPresent ✓ │  │ .jpg, .png, etc   │
   └─────┬────────────┘  └────┬──────────────┘
         │                    │
         └──────────┬─────────┘
                    │
            ┌───────▼────────┐
            │ All Valid?     │
            └───┬────────┬───┘
                │        │
            YES │        │ NO
                │        │
         ┌──────▼──┐  ┌──▼────────────────────┐
         │ Continue│  │ Return 400 BAD REQUEST│
         │ to      │  │                       │
         │ Service │  │ MethodArgumentNotValid│
         │         │  │ Exception            │
         └─────────┘  └──┬───────────────────┘
                         │
                    ┌────▼──────────────────────┐
                    │ GbobleExecptionHandler   │
                    │ Builds error response    │
                    │ with field errors       │
                    └────┬───────────────────┘
                         │
                    ┌────▼──────────────────┐
                    │ Return ApiResponse    │
                    │ {                     │
                    │   succeess: false,    │
                    │   message: "Failed",  │
                    │   payload: {          │
                    │     errors...         │
                    │   }                   │
                    │ }                     │
                    └───────────────────────┘
```

---

## 3️⃣ DTO Conversion Diagram

```
┌──────────────────────┐
│  CategoryRequest     │  (Client sends this)
│  (Input DTO)         │
│  - code: String      │
│  - name: String      │
│  - display: String   │
│  - imageUrl: String  │
│  - fromTime: DateTime│
│  - toTime: DateTime  │
└──────────┬───────────┘
           │
      ┌────▼──────────────────────┐
      │  CategoryMapper           │
      │  @Mapper(               │
      │    componentModel=spring) │
      │                           │
      │  toCategoryFromRequest()  │
      │  (Auto-generated)         │
      └────┬──────────────────────┘
           │
      ┌────▼──────────────┐
      │  Category         │  (Database entity)
      │  (Entity)         │
      │  - id: Long       │
      │  - code: String   │
      │  - name: String   │
      │  - display: String│
      │  - imageUrl: String
      │  - fromTime: DateTime
      │  - toTime: DateTime
      └────┬──────────────┘
           │
           │ Saved to DB
           │
      ┌────▼──────────────────────┐
      │  CategoryMapper           │
      │  toCategoryResponse()      │
      │  (Auto-generated)         │
      └────┬──────────────────────┘
           │
      ┌────▼─────────────────────┐
      │  CategoryResponse        │  (Server returns this)
      │  (Output DTO)            │
      │  - id: Long ✨ (NEW)     │
      │  - code: String          │
      │  - name: String          │
      │  - display: String       │
      │  - imageUrl: String      │
      │  - fromTime: DateTime    │
      │  - toTime: DateTime      │
      └──────────────────────────┘
```

---

## 4️⃣ Validation Annotation Hierarchy

```
┌────────────────────────────────────────────────────────────┐
│            Jakarta Bean Validation (JSR 380)              │
└────────────────────┬───────────────────────────────────────┘
                     │
        ┌────────────┴────────────┬──────────────────┐
        │                         │                  │
   ┌────▼────────┐         ┌──────▼────────┐   ┌────▼────────┐
   │ Null Check  │         │ String Check  │   │ Date Check  │
   └────┬────────┘         └──────┬────────┘   └────┬────────┘
        │                         │                  │
   ┌───┴────────┐      ┌──────┬──┴──┬──────┐   ┌────┴──────┐
   │   @Null    │      │      │     │      │   │           │
   │ @NotNull   │ ┌────▼──┐ ┌─▼──┐┌─▼───┐│   │ @Past      │
   │            │ │@NotEmpty│@Size││@Pattern
   │            │ └────┬──┘ └─┬──┘└─┬──┘│   │ @Future    │
   │            │      │      │    │   │   │ @PastOrPres│
   │            │      │      │    │   │   │            │
   └────────────┘ └─────┴──────┴────┴───┘   └────────────┘
```

---

## 5️⃣ Exception Handling Flow

```
┌────────────────────────────────────┐
│         Exception Thrown            │
└────┬───────────────────────────────┘
     │
     ├─────────────────────┬──────────────────┬─────────────────┐
     │                     │                  │                 │
┌────▼────────────────┐┌───▼──────────────┐┌──▼─────────────┐┌──▼──────────┐
│MethodArgumentNot    ││ ResourceNotFound ││ ApiExecption  ││ Exception  │
│ValidException       ││ Execption        ││              ││            │
└────┬─────────────────┘└───┬──────────────┘└──┬───────────┘└──┬────────┘
     │                      │                   │                │
     │ 400                  │ 404               │ 4xx/5xx        │ 500
     │ BAD_REQUEST          │ NOT_FOUND         │ (custom)       │ INTERNAL_ERROR
     │                      │                   │                │
┌────▼────────────────┐┌───▼──────────────┐┌──▼────────────────┐┌─▼──────────┐
│ GbobleException     ││                  ││                   ││            │
│ Handler catches all ││ Returns error    ││ Returns error    ││ Returns    │
│                     ││ response with    ││ response        ││ error      │
│ Builds error API    ││ 404 status       ││                  ││ response  │
│ Response with:      ││                  ││                  ││ with 500   │
│ - succeess: false   ││                  ││                  ││ status     │
│ - status: BAD_REQ   ││                  ││                  ││            │
│ - payload: {        ││                  ││                  ││            │
│     field_errors    ││                  ││                  ││            │
│   }                 ││                  ││                  ││            │
└─────────────────────┘└──────────────────┘└────────────────────┘└────────────┘
```

---

## 6️⃣ File Structure Tree

```
src/main/java/com/saranaresturantsystem/
│
├── Controllers/
│   └── Categories/
│       └── CategoryController.java ⭐ (UPDATED)
│           ├── GET /categories
│           ├── GET /categories/{id}
│           ├── POST /categories @Valid
│           ├── PUT /categories/{id} @Valid
│           └── DELETE /categories/{id}
│
├── DTO/
│   ├── Request/
│   │   └── CategoryRequest.java ⭐ (NEW)
│   │       ├── @NotBlank code
│   │       ├── @NotBlank name
│   │       ├── @ValidImageUrl imageUrl
│   │       └── @PastOrPresent dates
│   │
│   ├── Response/
│   │   ├── CategoryResponse.java ⭐ (NEW)
│   │   │   ├── id (DB generated)
│   │   │   ├── code
│   │   │   ├── name
│   │   │   └── ...
│   │   │
│   │   └── ApiResponse.java
│   │       ├── succeess: Boolean
│   │       ├── status: HttpStatus
│   │       ├── message: String
│   │       ├── payload: <T>
│   │       └── timestamp: Instant
│   │
│   ├── CategoryDTO.java
│   ├── PageDTO.java
│   └── PaginationDTO.java
│
├── Mappers/
│   └── CategoryMapper.java ⭐ (UPDATED)
│       ├── @Mapper(componentModel="spring")
│       ├── toCategoryFromRequest() ⭐ NEW
│       ├── toCategoryResponse() ⭐ NEW
│       └── ... (existing methods)
│
├── Services/
│   ├── Interfaces/
│   │   └── CategoryService.java ⭐ (UPDATED)
│   │       ├── getListCategory(int, int): Page<CategoryResponse>
│   │       ├── getCategoryById(Long): CategoryResponse
│   │       ├── createCategory(CategoryRequest): CategoryResponse
│   │       ├── updateCategory(Long, CategoryRequest): CategoryResponse
│   │       └── deleteCategory(Long): void
│   │
│   └── Implementation/
│       └── CategoryServiceImpl.java (NOT YET - YOUR TASK)
│
├── Repositories/
│   └── CategoryRepository.java (NOT YET - YOUR TASK)
│
├── Execption/
│   ├── GbobleExecptionHandler.java ⭐ (UPDATED)
│   │   ├── @ExceptionHandler(MethodArgumentNotValidException)
│   │   ├── @ExceptionHandler(ResourceNotFoundExecption)
│   │   ├── @ExceptionHandler(ApiExecption)
│   │   └── @ExceptionHandler(Exception)
│   │
│   ├── ApiExecption.java
│   ├── ResourceNotFoundExecption.java
│   └── ErrorResponse.java
│
├── Enities/
│   └── Category.java
│
└── Utils/
    ├── ValidationUtil.java ⭐ (NEW)
    │   ├── isValidCode()
    │   ├── isValidName()
    │   ├── isValidImageUrl()
    │   ├── isValidTimeRange()
    │   └── sanitizeInput()
    │
    └── Validation/
        ├── ValidImageUrl.java ⭐ (NEW)
        │   └── @interface ValidImageUrl
        │       @Constraint(validatedBy = ImageUrlValidator.class)
        │
        └── ImageUrlValidator.java ⭐ (NEW)
            └── implements ConstraintValidator<ValidImageUrl, String>
                └── isValid(): boolean
```

---

## 7️⃣ API Endpoint Sequence

```
Request Flow:
┌───────────────┐
│ Client sends  │
│  JSON to POST │
│  /categories  │
└────────┬──────┘
         │
         ├─ Deserialize JSON → CategoryRequest
         │
         ├─ @Valid triggers validation
         │
         ├─ Validation passes? ✓
         │      ↓ YES
         ├─ CategoryController.createCategory(request)
         │
         ├─ CategoryService.createCategory(request)
         │
         ├─ CategoryMapper.toCategoryFromRequest(request) → Category
         │
         ├─ CategoryRepository.save(category) → Category (with id)
         │
         ├─ CategoryMapper.toCategoryResponse(category) → CategoryResponse
         │
         ├─ Build ApiResponse<CategoryResponse>
         │
         └─ Return HTTP 201 CREATED + JSON response
```

---

## 8️⃣ Validation Rules Matrix

```
┌────────────┬──────────┬────┬─────┬─────────────────────────┐
│ Field      │ Required │ Min│ Max │ Validation              │
├────────────┼──────────┼────┼─────┼─────────────────────────┤
│ code       │    ✅    │ 2  │ 50  │ @NotBlank, @Size       │
│ name       │    ✅    │ 3  │100  │ @NotBlank, @Size       │
│ display    │    ❌    │ -  │ 50  │ @Size                  │
│ imageUrl   │    ❌    │ -  │500  │ @Size, @ValidImageUrl  │
│ fromTime   │    ❌    │ -  │ -   │ @PastOrPresent         │
│ toTime     │    ❌    │ -  │ -   │ @PastOrPresent         │
└────────────┴──────────┴────┴─────┴─────────────────────────┘
```

---

## 9️⃣ Success vs Error Response

```
SUCCESS Response (200/201)          ERROR Response (400)
┌─────────────────────────────┐    ┌──────────────────────────────┐
│ {                           │    │ {                            │
│   "succeess": true,         │    │   "succeess": false,         │
│   "status": "CREATED",      │    │   "status": "BAD_REQUEST",   │
│   "message": "Created",     │    │   "message": "Validation...",│
│   "payload": {              │    │   "payload": {               │
│     "id": 1,                │    │     "code": "Min 2 chars",   │
│     "code": "PIZZA",        │    │     "name": "Min 3 chars",   │
│     "name": "Pizza...",     │    │     "imageUrl": "Invalid.." │
│     "display": "Pizzas",    │    │   },                         │
│     "imageUrl": "..."       │    │   "timestamp": "2026-03-30..."
│   },                         │    │ }                            │
│   "timestamp": "2026-03-30" │    │
│ }                            │    │
└─────────────────────────────┘    └──────────────────────────────┘
```

---

## 🔟 Custom Validator Creation Pattern

```
Step 1: Create Annotation Interface
┌─────────────────────────────────────┐
│ @Target({ElementType.FIELD})        │
│ @Retention(RUNTIME)                 │
│ @Constraint(validatedBy=...)        │
│ public @interface @ValidImageUrl {} │
└─────────────────────────────────────┘
                │
                ▼
Step 2: Implement Validator
┌──────────────────────────────────┐
│ ConstraintValidator<...>         │
│ ├─ @Override isValid() {         │
│ │   return checkValidation();    │
│ │ }                              │
│ └─ Returns true/false            │
└──────────────────────────────────┘
                │
                ▼
Step 3: Use in DTO
┌──────────────────────────────────┐
│ @ValidImageUrl                   │
│ private String imageUrl;         │
└──────────────────────────────────┘
                │
                ▼
Step 4: Spring Validates Automatically
┌──────────────────────────────────┐
│ When @Valid annotation triggered │
│ ├─ Call isValid()                │
│ ├─ Get validation result         │
│ └─ Throw if invalid              │
└──────────────────────────────────┘
```

---

**Diagrams Version**: 1.0  
**Last Updated**: March 30, 2026  
**Status**: Complete ✅

