package utils;

import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.util.ArrayList;

public class ExtensionFileFilter extends FileFilter {
    private String description;
    private ArrayList<String> extensions=new ArrayList<>();
    //自定义方法，用于添加文件后缀名
    public void addExtension(String extension) {
        if(!extension.startsWith("."))
            extension="."+extension;
        extensions.add(extension.toLowerCase());
    }
    //用于设置该文件过滤器的描述文本
    public void setDescription(String description) {
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public boolean accept(File file) {
        if(file.isDirectory()) return true;
        String name=file.getName().toLowerCase();
        for(String extension:extensions) {
            if(name.endsWith(extension)) return true;
        }
        return false;
    }
}