import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

import net.sourceforge.barbecue.Barcode;
import net.sourceforge.barbecue.BarcodeFactory;
import net.sourceforge.barbecue.BarcodeImageHandler;

public class EANcodes {
    
    public static void main(String[] args) {
        
        // Pedir al usuario los datos del producto
        Scanner scanner = new Scanner(System.in);
        System.out.print("Introduce el nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Introduce el código del producto: ");
        String codigo = scanner.nextLine();
        System.out.print("Introduce el precio del producto: ");
        double precio = scanner.nextDouble();
        
        // Crear el objeto Producto con los datos introducidos por el usuario
        Producto producto = new Producto(nombre, codigo, precio);
        
        // Generar el código de barras para el producto
        try {
            producto.generarCodigoBarras();
            System.out.println("Código de barras generado correctamente para el producto " + producto.getNombre());
        } catch (IOException e) {
            System.out.println("Error al generar el código de barras para el producto " + producto.getNombre());
            e.printStackTrace();
        }
        
        // Guardar el producto en un archivo
        try {
            producto.guardarProducto();
            System.out.println("Producto guardado correctamente en el archivo " + producto.getNombreArchivo());
        } catch (IOException e) {
            System.out.println("Error al guardar el producto en el archivo " + producto.getNombreArchivo());
            e.printStackTrace();
        }
    }
}

class Producto {
    
    private String nombre;
    private String codigo;
    private double precio;
    
    public Producto(String nombre, String codigo, double precio) {
        this.nombre = nombre;
        this.codigo = codigo;
        this.precio = precio;
    }
    
    public String getNombre() {
        return nombre;
    }
    
    public String getCodigo() {
        return codigo;
    }
    
    public double getPrecio() {
        return precio;
    }
    
    public String getNombreArchivo() {
        return "Productos/" + nombre + ".txt";
    }
    
    public void generarCodigoBarras() throws IOException {
        
        // Crear el objeto Barcode con el código del producto
        Barcode barcode = BarcodeFactory.createCode128(codigo);
        
        // Agregar el nombre del producto y el precio como texto debajo del código de barras
        barcode.setLabel(nombre + " - $" + precio);
        
        // Crear una imagen de la etiqueta del producto
        BufferedImage image = new BufferedImage(300, 100, BufferedImage.TYPE_INT_RGB);
        
        // Convertir el objeto Barcode en una imagen de código de barras y añadirla a la imagen de la etiqueta
        BarcodeImageHandler.writePNG(barcode, image);
        
        // Guardar la imagen de la etiqueta en un archivo con el nombre del producto en la carpeta "CodigosEAN"
        String fileName = "CodigosEAN/" + nombre + ".png";
        File outputFile = new File(fileName);
        if (!outputFile.getParentFile().exists()) {
            // Si la carpeta "CodigosEAN" no existe, crearla
            outputFile.getParentFile().mkdirs();
        }
        // Guardar la imagen en el archivo
        BarcodeImageHandler.writePNG(barcode, outputFile);
    }
    
    public void guardarProducto() throws IOException {
        
        // Crear el archivo para el producto en la carpeta "Productos"
        String fileName = getNombreArchivo();
        File outputFile = new File(fileName);
        if (!outputFile.getParentFile().exists()) {
        // Si la carpeta "Productos" no existe, crearla
        outputFile.getParentFile().mkdirs();
    }
        // Guardar los datos del producto en el archivo
        String data = "Nombre: " + nombre + "\n" + "Código: " + codigo + "\n" + "Precio: $" + precio;
        FileUtils.writeStringToFile(outputFile, data, "UTF-8");
    }
    }
