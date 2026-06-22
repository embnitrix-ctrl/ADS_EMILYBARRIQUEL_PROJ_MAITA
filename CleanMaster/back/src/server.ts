import dotenv from 'dotenv';
dotenv.config();

import 'reflect-metadata';
import app from './app';
import { AppDataSource } from './config/datasource';

const PORT = process.env.API_PORT || 3001;

async function startServer() {
  try {
    await AppDataSource.initialize();
    console.log("Data Source inicializado!");
    app.listen(PORT, () => {
      console.log(`Servidor rodando na porta ${PORT}`);
    });
  } catch (error) {
    console.error("Erro na inicialização:", error);
  }
}

startServer();