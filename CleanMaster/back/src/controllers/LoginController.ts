import { Request, Response } from "express";
import { AppDataSource } from "../config/datasource";
import { User } from "../entities/User";
import bcrypt from 'bcryptjs';
import jwt from 'jsonwebtoken';

export class LoginController {
  static async login(req: Request, res: Response) {
    const { email, password } = req.body;
    const repo = AppDataSource.getRepository(User);

    try {
      const user = await repo.findOneBy({ email });
      if (!user) {
        return res.status(401).json({ message: "Credenciais inválidas" });
      }

  
      const validPassword = await bcrypt.compare(password, user.password_hash);
      if (!validPassword) {
        return res.status(401).json({ message: "Credenciais inválidas" });
      }

      // Gera tk
      const token = jwt.sign({ id: user.id }, process.env.JWT_SECRET, { expiresIn: '1d' });
      return res.json({ token });

    } catch (error) {
      console.error("ERRO NO LOGIN CONTROLLER:", error);
      return res.status(500).json({ message: "Erro interno no servidor" });
    }
  }
}