# DrawPanel: Project of OO

This is a drawing software based on Java Swing. This project is my practice of applying design patterns.

## Project Structure（v0.2.1）

> This image may be out of date.

![Structure](structure.png)

## Design Patterns

Shape extends serializable: Abstract Factory & Composite   
Shape manager extends serializable: Singleton  
StringDecorator implement shape, serializable: Decorator  
Canvas: State  
Commands: Command  
Save@Load: Prototype
Copy: Prototype

## TODOs:

基础功能需求:
1. 设计良好的图形用户界面，界面中要求至少有默认大小的三角形、方框、圆
   形、椭圆、连接线等五种元素可供用户选择后，绘制到画布上;
   1. 目前已经有圆形，其他图形在其他功能完善后加入。
2. 允许用户添加文字描述;
3. 单击可以选中图形，并允许对图形的拷贝复制; ✅
4. 多个图形可以组合，组合后的图形同样有拷贝复制的功能;
5. 支持撤销上一步操作的功能。✅

扩展功能需求:
1. 支持图形(包括组合图形)的拖拽调整图形大小; ✅
2. 支持撤销多步的功能; ✅
3. 设计一种硬盘文件存储格式可以保存用户绘制的图形，并可以加载。✅

## Warning:

本项目使用 MIT 开源许可证，在软件和软件（部分或整体）的所有副本中都必须包含相关著作权声明和许可声明（LICENSE）。 

The related copyright notice and permission notice shall be included in all copies or substantial portions of the Software.