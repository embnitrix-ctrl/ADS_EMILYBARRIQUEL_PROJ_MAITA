import { Request, Response } from "express";
import { AppDataSource } from "../config/datasource";
import { User } from "../entities/User";
import bcrypt from 'bcryptjs';

export class UserController {
  // --- CRIAR USUÁRIO (Já existia) ---
  static async create(req: Request, res: Response) {
    const { email, password } = req.body;
    const repo = AppDataSource.getRepository(User);

    if (!email || !password) {
      return res.status(400).json({ message: "Email e senha são obrigatórios" });
    }

    try {
      const password_hash = await bcrypt.hash(password, 8);

      const user = repo.create({ email, password_hash });
      await repo.save(user);

      return res.status(201).json({ id: user.id, email: user.email });
    } catch (error: any) {
      // Tratamento para email duplicado (código de erro do Postgres)
      if (error.code === '23505') {
        return res.status(409).json({ message: "Este email já está em uso." });
      }
      console.error(error);
      return res.status(500).json({ message: "Erro ao criar usuário" });
    }
  }

  // --- LISTAR USUÁRIOS (NOVO - Para a tela /usuarios) ---
  static async getAll(req: Request, res: Response) {
    const repo = AppDataSource.getRepository(User);
    try {
      const users = await repo.find({
        // IMPORTANTE: Trazemos apenas id e email. A senha NUNCA deve vir.
        select: {
          id: true,
          email: true
        }
      });
      return res.json(users);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao listar usuários" });
    }
  }
}