import { Router } from "express";
import categoryRouter from "./CategoryRoutes";
import productRouter from "./ProductRoutes";
import userRouter from "./UserRoutes";

const router = Router();

router.use('/', userRouter);
router.use('/categories', categoryRouter);
router.use('/products', productRouter);

export default router;