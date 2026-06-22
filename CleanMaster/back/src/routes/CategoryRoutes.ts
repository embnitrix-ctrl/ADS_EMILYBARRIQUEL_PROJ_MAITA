import { Router } from "express";
import { CategoryController } from "../controllers/CategoryController";
import { authMiddleware } from "../middlewares/auth";
import { ProductController } from "../controllers/ProductController";

const router = Router();

// pub
router.get('/', CategoryController.getAll);
router.get('/:id', CategoryController.getById);
router.get('/:id/products', ProductController.getByCategoryId);

// prot
router.post('/', authMiddleware, CategoryController.create);
router.put('/:id', authMiddleware, CategoryController.update);
router.delete('/:id', authMiddleware, CategoryController.delete);

export default router;