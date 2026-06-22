import { Router } from "express";
import { UserController } from "../controllers/UserController";
import { LoginController } from "../controllers/LoginController";
import { authMiddleware } from "../middlewares/auth"; // Importante: Importar o middleware

const router = Router();

// Rotas Públicas (Cadastro e Login)
router.post('/users', UserController.create);
router.post('/login', LoginController.login);

// --- NOVA ROTA ADICIONADA ---
// Rota Protegida para listar usuários (usada na tela /usuarios do front)
router.get('/users', authMiddleware, UserController.getAll);

export default router;