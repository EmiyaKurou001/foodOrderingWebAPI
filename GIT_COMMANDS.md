# Git Commands for Food Ordering API

## 1. Stage the deleted Product stub files:
```bash
git add src/main/java/com/foodordering/model/entity/Product.java
git add src/main/java/com/foodordering/repository/ProductRepository.java
```

## 2. Stage all new files (repositories, services, controllers, DTOs, exception handler):
```bash
git add src/main/java/com/foodordering/repository/
git add src/main/java/com/foodordering/service/
git add src/main/java/com/foodordering/controller/
git add src/main/java/com/foodordering/dto/
git add src/main/java/com/foodordering/exception/
```

## 3. Or stage everything at once:
```bash
git add .
```

## 4. Check what will be committed:
```bash
git status
```

## 5. Commit all changes:
```bash
git commit -m "Add repositories, services, controllers, DTOs and exception handler

- Add repositories for Account, Category, MenuItem, Role
- Add service layer with interfaces and implementations
- Add REST controllers with full CRUD endpoints
- Add request/response DTOs with validation
- Add global exception handler
- Remove Product and ProductRepository stub files"
```

## 6. (Optional) Push to remote:
```bash
git push origin entity
```

## Alternative: Stage and commit in one step (if you want to review first, use steps 1-4 above):
```bash
git add .
git commit -m "Add complete API layer: repositories, services, controllers, DTOs and exception handler"
```

