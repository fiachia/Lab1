package run;
import java.io.*;
import java.security.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.crypto.*;
public class Run {
    public static void main(String[] args) throws Exception {
        RunStart run = new RunStart();
        run.runStart();
        System.out.println("----感谢使用命令行文件管理系统----");
    }

}
class RunStart {
    private String path = "C:" + File.separator + "Users" + File.separator + System.getenv().get("USERNAME");
    private Pattern pan0 = Pattern.compile("[a-zA-Z]:\\\\?");
    void getHelp() {
        System.out.println("mkdir：创建文件");
        System.out.println("rm：删除文件/文件夹");
        System.out.println("cd：进入文件");
        System.out.println("ls：打印文件列表");
        System.out.println("cp：复制文件");
        System.out.println("cpa：复制文件夹");
        System.out.println("sl：加密文件");
        System.out.println("rsl：解密文件");
        System.out.println("quit：退出");
    }
    void runStart() throws Exception{
        System.out.println("----欢迎使用命令行文件管理系统----");
        Scanner s=new Scanner(System.in);
        String a;
        do {
            System.out.print(path + " >");
            a=s.nextLine();
            switch(a) {
                case "mkdir"://创建文件夹
                    createFile();
                    break;
                case "rm"://删除文件或文件夹
                    removeFile();
                    break;
                case "cd"://打开文件夹
                    intoFile();
                    break;
                case "ls"://列出目录（带颜色为文件夹
                    //本来想弄绿色的，结果32颜色显示为黄色，可能是内置控制台的原因
                    listFile();
                    break;
                case "cp"://复制文件
                    copyFile();
                    break;
                case "cpa"://复制文件夹
                    copyAllFile();
                    break;
                case "sl":
                    saleFile();
                    break;
                case "rsl":
                    resaleFile();
                    break;
                case "quit"://退出
                    a = "quit";
                    System.out.println("程序已退出");
                    break;
                case "--help"://帮助
                    getHelp();
                    break;
                case "":
                    break;
                default:
                    System.out.println("没有相应命令，你可以输入--help查看相应信息");
            }
        }while(!a.equals("quit"));
    }
    void createFile() {//创建文件夹
        Scanner s = new Scanner(System.in);
        System.out.print("请输入文件名：");
        String Filename = s.next();
        File file = new File(path + File.separator + Filename);
        System.out.println(path + File.separator + Filename);
        if (!file.exists()) {
            file.mkdirs();
            System.out.println("文件夹创建成功");
        } else {
            System.out.println("该文件夹已存在");
        }
    }
    void removeFile(){
        Scanner s = new Scanner(System.in);
        System.out.print("请输入文件名：");
        String Filename = s.next();
        removeFile(path + File.separator + Filename);
    }
    void removeFile(String fPath){
        File f = new File(fPath);
        if (f.exists()){
            File[] files =f.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        removeFile(file.getPath());
                    } else {
                        file.delete();
                        System.out.println("删除" + file.getPath());
                    }
                }
            }
            f.delete();//删除当前目录
            System.out.println("删除" + f.getPath());
        }else {
            System.out.println("没有找到" + fPath);
        }
    }
    void listFile() {
        File f1 = new File(path);
        if (f1.isDirectory()) {
            String[] s = f1.list();
            if (s!=null){
                int i = -1;
                StringBuilder longName = new StringBuilder();
                for (String value : s) {
                    int longNum = value.length() + getChineseNumber(value);
                    if (longNum < 20){
                        i++;
                        if (i > 5) {
                            System.out.print("\n");
                            i = 0;
                        }
                        String span = " ";
                        String str =  span.repeat(20-longNum);
                        File f = new File(path + File.separator + value);
                        if (f.isDirectory()) {
                            System.out.print("\033[32m" + value + "\033[0m" + str);
                        } else {
                            System.out.print(value + str);
                        }

                    } else if (longNum < 40){
                        i+=2;
                        if (i > 5) {
                            System.out.print("\n");
                            i = 1;
                        }
                        String span = " ";
                        String str =  span.repeat(40-longNum);
                        File f = new File(path + File.separator + value);
                        if (f.isDirectory()) {
                            System.out.print("\033[32m" + value + "\033[0m" + str);
                        } else {
                            System.out.print(value + str);
                        }

                    }else if (longNum < 60){
                        i+=3;
                        if (i > 5) {
                            System.out.print("\n");
                            i = 2;
                        }
                        String span = " ";
                        String str =  span.repeat(60-longNum);
                        File f = new File(path + File.separator + value);
                        if (f.isDirectory()) {
                            System.out.print("\033[32m" + value + "\033[0m" + str);
                        } else {
                            System.out.print(value + str);
                        }

                    }else {
                        File f = new File(path + File.separator + value);
                        if (f.isDirectory()) {
                            longName.append("\033[32m").append(value).append("\033[0m").append("\n");
                        } else {
                            longName.append(value).append("\n");
                        }
                    }
                }
                System.out.println();
                if (!longName.toString().equals("")){
                    System.out.print(longName);
                }
            } else {
                System.out.println();
            }
        } else {
            System.out.println(path + " 不是一个目录");
        }
    }
    void intoFile() throws IOException{
        Scanner s = new Scanner(System.in);
        System.out.print("请输入文件名：");
        String Filename = s.next();
        if (pan0.matcher(path).matches() & pan0.matcher(Filename).matches()){
            File f1 = new File(Filename);
            if (f1.exists()){
                //path = f1.getCanonicalPath();
                path = Filename;
            } else {
                System.out.println("没有找到盘符");
            }
        }else{
            File f1 = new File(path + File.separator + Filename);
            if (f1.exists()){
                if (f1.isDirectory()){
                    //path = path + File.separator + Filename;
                    //path = f1.getAbsolutePath();
                    path = f1.getCanonicalPath();
                }else {
                    System.out.println(path + File.separator + Filename + "不是一个目录");
                }
            }else {
                System.out.println("没有找到" + path + File.separator + Filename);
            }
        }

    }
    void copyFile() throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.print("请输入原文件名：");
        String oldPath = s.next();
        System.out.print("请输入新文件名：");
        String newPath = s.next();
        File f = new File(path + File.separator + newPath);
        if (f.exists()){
            System.out.println("已有文件名，是否覆盖？");
            String a = s.next();
            if (a.equals("y") | a.equals("Y")){
                copyFile(path + File.separator + oldPath,path + File.separator + newPath);
            }else {
                System.out.println("已结束复制操作");
            }
        }else {
            copyFile(path + File.separator + oldPath,path + File.separator + newPath);
        }
    }
    void copyFile(String oldPath, String newPath) throws IOException {
        File in = new File(oldPath);
        if (in.exists()){
            if (in.isFile()){
                File out = new File(newPath);
                FileInputStream inFile = new FileInputStream(in);
                FileOutputStream outFile = new FileOutputStream(out);
                byte[] buffer = new byte[1024];
                int i;
                while (-1 != (i = inFile.read(buffer))) {
                    outFile.write(buffer, 0, i);
                }//end while
                inFile.close();
                outFile.close();
                System.out.println("复制成功");
            } else {
                System.out.println("不能复制文件夹");
            }
        } else {
            System.out.println("没有找到" + oldPath);
        }
    }
    void copyAllFile() throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.print("请输入原文件名：");
        String oldPath = s.next();
        System.out.print("请输入新文件名：");
        String newPath = s.next();
        File f = new File(path + File.separator + newPath);
        if (f.exists()){
            System.out.println("已有文件名，是否覆盖？");
            String a = s.next();
            if (a.equals("y") | a.equals("Y")){
                copyAllFile(path + File.separator + oldPath,path + File.separator + newPath);
            }else {
                System.out.println("已结束复制操作");
            }
        }else {
            copyAllFile(path + File.separator + oldPath,path + File.separator + newPath);
        }
    }
    void copyAllFile(String oldPath,String newPath) throws IOException {
        File f=new File(oldPath);
        if (f.exists()){
            if (f.isDirectory()){
                File f0=new File(newPath);
                f0.mkdirs(); //如果文件夹不存在 则建立新文件夹
                String[] file=f.list();
                File temp;
                if (file!=null){
                    for (String s : file) {
                        if (oldPath.endsWith(File.separator)) {
                            temp = new File(oldPath + s);
                        } else {
                            temp = new File(oldPath + File.separator + s);
                        }
                        if (temp.isFile()) {
                            FileInputStream input = new FileInputStream(temp);
                            FileOutputStream output = new FileOutputStream(newPath + File.separator + temp.getName());
                            byte[] b = new byte[1024 * 5];
                            int len;
                            while ((len = input.read(b)) != -1) {
                                output.write(b, 0, len);
                            }
                            output.flush();
                            output.close();
                            input.close();
                            System.out.println("复制" + temp.getPath());
                        }
                        if (temp.isDirectory()) {//如果是子文件夹
                            copyAllFile(oldPath + File.separator + s, newPath + File.separator + s);
                        }
                    }
                    System.out.println("复制" + f.getPath());
                }

            } else {
                System.out.println("不能复制文件");
            }
        }else {
            System.out.println("没有找到" + oldPath);
        }
    }
    int getChineseNumber(String str) {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|！，。（）《》“”？：；【】]");
        Matcher m = p.matcher(str);
        int count = 0;
        while(m.find()){
            count ++;
        }
        return count - count/5;
    }
    void saleFile() throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.print("请输入要加密的文件名：");
        String fPath = s.next();
        System.out.println("请注意，您正在使用加密功能，请确保您能记住密码以防文件无法恢复");
        System.out.println("请注意，我们没有保存您的密码，而且您的每一步操作都是不可逆的。");
        System.out.print("请输入密码（六位数字）：");
        int key = getInt();
        File file = new File(path + File.separator + fPath);
        if(file.exists()){
            saleFile(file.getPath(),key);
        } else {
            System.out.println("没有找到加密文件");
        }
        new File(path + File.separator + "temp").delete();
    }
    void saleFile(String fPath,int key) throws IOException {
        File f=new File(fPath);
        String tempPath = path + File.separator + "temp";
        File f0=new File(tempPath);
        f0.mkdirs(); //如果文件夹不存在 则建立新文件夹
        if (f.exists()){
            if (f.isFile()){
                String oldPath = f.getPath();
                FileInputStream input = new FileInputStream(f);
                File tempFile = new File(tempPath + File.separator + f.getName());
                FileOutputStream output = new FileOutputStream(tempPath + File.separator + f.getName());
                byte[] b1 = new byte[1024];
                byte[] b2 = new byte[1024];
                int len;
                while ((len = input.read(b1)) != -1) {
                    for(int i=0;i<len;i++)
                    {
                        byte b=b1[i];
                        if (i%3 == 0){
                            b2[i] = (byte) (b + (byte) (key>>14));
                        }else if (i%3 == 1){
                            b2[i] = (byte) (b + (byte) (key>>7));
                        }else {
                            b2[i] = (byte) (b + (byte) (key));
                        }
                    }
                    output.write(b2, 0, len);
                }
                output.flush();
                output.close();
                input.close();
                f.delete();
                tempFile.renameTo(new File(oldPath));
                System.out.println("加密" + oldPath);
            } else {
                String[] file=f.list();
                File temp;
                if (file!=null){
                    for (String s : file) {
                        if (fPath.endsWith(File.separator)) {
                            temp = new File(fPath + s);
                        } else {
                            temp = new File(fPath + File.separator + s);
                        }
                        if (temp.isFile()) {
                            String oldPath = temp.getPath();
                            FileInputStream input = new FileInputStream(temp);
                            File tempFile = new File(tempPath + File.separator + temp.getName());
                            FileOutputStream output = new FileOutputStream(tempPath + File.separator + temp.getName());
                            byte[] b1 = new byte[1024];
                            byte[] b2 = new byte[1024];
                            int len;
                            while ((len = input.read(b1)) != -1) {
                                for(int i=0;i<len;i++)
                                {
                                    byte b=b1[i];
                                    if (i%3 == 0){
                                        b2[i] = (byte) (b + (byte) (key>>14));
                                    }else if (i%3 == 1){
                                        b2[i] = (byte) (b + (byte) (key>>7));
                                    }else {
                                        b2[i] = (byte) (b + (byte) (key));
                                    }
                                }
                                output.write(b2, 0, len);
                            }
                            output.flush();
                            output.close();
                            input.close();
                            temp.delete();
                            tempFile.renameTo(new File(oldPath));
                            System.out.println("加密" + oldPath);
                        } else if (temp.isDirectory()) {//如果是子文件夹
                            saleFile(fPath + File.separator + s, key);
                        }
                    }
                    System.out.println("加密" + f.getPath());
                }
            }

        }else {
            System.out.println("没有找到" + fPath);
        }
    }
    void resaleFile() throws IOException {
        Scanner s = new Scanner(System.in);
        System.out.print("请输入要解密的文件名：");
        String fPath = s.next();
        System.out.println("请注意，您正在使用解密功能，请确保您使用正确的密码以防文件无法恢复");
        System.out.println("请注意，我们没有保存您的密码，而且您的每一步操作都是不可逆的。");
        System.out.println("如果你真的忘记了密码，请记住所有你输入的密码，以便恢复到最初的加密状态");
        System.out.print("请输入密码：");
        int key = getInt();
        File file = new File(path + File.separator + fPath);
        if(file.exists()){
            resaleFile(file.getPath(),key);
        } else {
            System.out.println("没有找到解密文件");
        }
        new File(path + File.separator + "temp").delete();
    }
    void resaleFile(String fPath,int key) throws IOException {
        File f=new File(fPath);
        String tempPath = path + File.separator + "temp";
        File f0=new File(tempPath);
        f0.mkdirs(); //如果文件夹不存在 则建立新文件夹
        if (f.exists()){
            if (f.isFile()){
                String oldPath = f.getPath();
                FileInputStream input = new FileInputStream(f);
                File tempFile = new File(tempPath + File.separator + f.getName());
                FileOutputStream output = new FileOutputStream(tempPath + File.separator + f.getName());
                byte[] b1 = new byte[1024];
                byte[] b2 = new byte[1024];
                int len;
                while ((len = input.read(b1)) != -1) {
                    for(int i=0;i<len;i++)
                    {
                        byte b=b1[i];
                        if (i%3 == 0){
                            b2[i] = (byte) (b - (byte) (key>>14));
                        }else if (i%3 == 1){
                            b2[i] = (byte) (b - (byte) (key>>7));
                        }else {
                            b2[i] = (byte) (b - (byte) (key));
                        }
                    }
                    output.write(b2, 0, len);
                }
                output.flush();
                output.close();
                input.close();
                f.delete();
                tempFile.renameTo(new File(oldPath));
                System.out.println("加密" + oldPath);
            } else {
                String[] file=f.list();
                File temp;
                if (file!=null){
                    for (String s : file) {
                        if (fPath.endsWith(File.separator)) {
                            temp = new File(fPath + s);
                        } else {
                            temp = new File(fPath + File.separator + s);
                        }
                        if (temp.isFile()) {
                            String oldPath = temp.getPath();
                            FileInputStream input = new FileInputStream(temp);
                            File tempFile = new File(tempPath + File.separator + temp.getName());
                            FileOutputStream output = new FileOutputStream(tempPath + File.separator + temp.getName());
                            byte[] b1 = new byte[1024];
                            byte[] b2 = new byte[1024];
                            int len;
                            while ((len = input.read(b1)) != -1) {
                                for(int i=0;i<len;i++)
                                {
                                    byte b=b1[i];
                                    if (i%3 == 0){
                                        b2[i] = (byte) (b - (byte) (key>>14));
                                    }else if (i%3 == 1){
                                        b2[i] = (byte) (b - (byte) (key>>7));
                                    }else {
                                        b2[i] = (byte) (b - (byte) (key));
                                    }
                                }
                                output.write(b2, 0, len);
                            }
                            output.flush();
                            output.close();
                            input.close();
                            temp.delete();
                            tempFile.renameTo(new File(oldPath));
                            System.out.println("解密" + oldPath);
                        } else if (temp.isDirectory()) {//如果是子文件夹
                            resaleFile(fPath + File.separator + s, key);
                        }
                    }
                    System.out.println("解密" + f.getPath());
                }
            }
        }else {
            System.out.println("没有找到" + fPath);
        }
    }
    int getInt(){
        Scanner s = new Scanner(System.in);
        int key;
        if (s.hasNextInt()){
            key = s.nextInt();
            while (key < 1 | key > 999999) {
                System.out.println("密码不在范围内");
                System.out.print("请输入密码（六位数字）：");
                key = s.nextInt();
            }
            return key;
        } else {
            System.out.println("请输入数字！");
            System.out.print("请输入密码（六位数字）：");
            return getInt();
        }
    }
}

