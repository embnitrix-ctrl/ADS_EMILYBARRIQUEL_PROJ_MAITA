import { Request, Response } from "express";
import { AppDataSource } from "../config/datasource";
import { Category } from "../entities/Category";

const repo = AppDataSource.getRepository(Category);

export class CategoryController {
  // LIST all
  static async getAll(req: Request, res: Response) {
    try {
      const categories = await repo.find({ order: { name: 'ASC' } });
      return res.status(200).json(categories);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao buscar categorias" });
    }
  }

  // BUSCAR
  static async getById(req: Request, res: Response) {
    const { id } = req.params;
    try {
      const category = await repo.findOneBy({ id: Number(id) });
      if (!category) {
        return res.status(404).json({ message: "Categoria não encontrada" });
      }
      return res.status(200).json(category);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao buscar categoria" });
    }
  }

  // CRIAR
  static async create(req: Request, res: Response) {
    const { name } = req.body;
    try {
      if (!name) {
        return res.status(400).json({ message: "O nome é obrigatório" });
      }
      const category = repo.create({ name });
      await repo.save(category);
      return res.status(201).json(category);
    } catch (error) {
      if (error.code === '23505') {
        return res.status(409).json({ message: "Uma categoria com este nome já existe." });
      }
      console.error(error);
      return res.status(500).json({ message: "Erro ao criar categoria" });
    }
  }

  // ATUALIZAR
  static async update(req: Request, res: Response) {
    const { id } = req.params;
    const { name } = req.body;
    try {
      const category = await repo.findOneBy({ id: Number(id) });
      if (!category) {
        return res.status(404).json({ message: "Categoria não encontrada" });
      }
      if (name) {
        category.name = name;
        await repo.save(category);
      }
      return res.status(200).json(category);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao atualizar categoria" });
    }
  }

  // DELETAR
  static async delete(req: Request, res: Response) {
    const { id } = req.params;
    try {
      const category = await repo.findOneBy({ id: Number(id) });
      if (!category) {
        return res.status(404).json({ message: "Categoria não encontrada" });
      }
      await repo.remove(category);
      return res.status(204).send(); 
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao deletar categoria" });
    }
  }
}