import { Request, Response } from "express";
import { AppDataSource } from "../config/datasource";
import { Product } from "../entities/Product";
import { Category } from "../entities/Category";

const productRepo = AppDataSource.getRepository(Product);
const categoryRepo = AppDataSource.getRepository(Category);

export class ProductController {
  // LISTA all
  static async getAll(req: Request, res: Response) {
    try {
      const products = await productRepo.find({
        relations: ['category'],
        order: { name: 'ASC' }
      });
      return res.status(200).json(products);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao buscar produtos" });
    }
  }

  // BUSCAR POR ID
  static async getById(req: Request, res: Response) {
    const { id } = req.params;
    try {
      const product = await productRepo.findOne({
        where: { id: Number(id) },
        relations: ['category']
      });
      if (!product) {
        return res.status(404).json({ message: "Produto não encontrado" });
      }
      return res.status(200).json(product);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao buscar produto" });
    }
  }

  // --- NOVO MÉTODO ADICIONADO ---
  // BUSCAR POR ID DA CATEGORIA
  static async getByCategoryId(req: Request, res: Response) {
    const { id } = req.params;
    try {
      // Busca produtos onde a categoria.id é igual ao id da URL
      const products = await productRepo.find({
        where: { category: { id: Number(id) } },
        relations: ['category']
      });
      return res.status(200).json(products);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao buscar produtos da categoria" });
    }
  }
  // ------------------------------
  
  // CRIAR
  static async create(req: Request, res: Response) {
    const { name, price, categoryId } = req.body;
    try {
      if (!name || !price || !categoryId) {
        return res.status(400).json({ message: "Nome, preço e categoryId são obrigatórios" });
      }
      const category = await categoryRepo.findOneBy({ id: categoryId });
      if (!category) {
        return res.status(404).json({ message: "Categoria não encontrada" });
      }
      const product = productRepo.create({ name, price, category });
      await productRepo.save(product);
      return res.status(201).json(product);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao criar produto" });
    }
  }

  // ATUALIZAR
  static async update(req: Request, res: Response) {
    const { id } = req.params;
    const { name, price, categoryId } = req.body;
    try {
      const product = await productRepo.findOneBy({ id: Number(id) });
      if (!product) {
        return res.status(404).json({ message: "Produto não encontrado" });
      }
      if (name) product.name = name;
      if (price) product.price = price;
      if (categoryId) {
        const category = await categoryRepo.findOneBy({ id: categoryId });
        if (!category) {
          return res.status(404).json({ message: "Categoria não encontrada para atualização" });
        }
        product.category = category;
      }
      await productRepo.save(product);
      return res.status(200).json(product);
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao atualizar produto" });
    }
  }

  // DELETAR
  static async delete(req: Request, res: Response) {
    const { id } = req.params;
    try {
      const product = await productRepo.findOneBy({ id: Number(id) });
      if (!product) {
        return res.status(404).json({ message: "Produto não encontrado" });
      }
      await productRepo.remove(product);
      return res.status(204).send(); 
    } catch (error) {
      console.error(error);
      return res.status(500).json({ message: "Erro ao deletar produto" });
    }
  }
}