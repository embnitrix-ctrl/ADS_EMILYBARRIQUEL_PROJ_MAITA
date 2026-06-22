import { Entity, PrimaryGeneratedColumn, Column, ManyToOne } from "typeorm";
import { Category } from "./Category";
@Entity('products')
export class Product {
  @PrimaryGeneratedColumn()
  id: number;
  @Column()
  name: string;
  @Column('decimal', { precision: 10, scale: 2 })
  price: number;
  @ManyToOne(() => Category, category => category.products)
  category: Category;
}