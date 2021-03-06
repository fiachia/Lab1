package run;
import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    private void getHelp() {
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
    public void runStart() {
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
    private void createFile() {//创建文件夹
        try {
            Scanner s = new Scanner(System.in);
            System.out.print("请输入文件名：");
            String Filename = getPath(s.next());
            File file = new File(Filename);
            System.out.println(Filename);
            if (!file.exists()) {
                file.mkdirs();
                System.out.println("文件夹创建成功");
            } else {
                System.out.println("该文件夹已存在");
            }
        } catch (Exception e){
            System.out.println("创建文件夹（mkdir）指令失败");
            System.out.println(e);
        }
    }
    private void removeFile(){
        Scanner s = new Scanner(System.in);
        System.out.print("请输入文件名：");
        String Filename = getPath(s.next());
        removeFile(Filename);
    }
    private void removeFile(String fPath){
        try {
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
        } catch (Exception e) {
            System.out.println("删除文件/文件夹（rm）指令失败");
            System.out.println(e);
        }
    }
    private void listFile() {
        try {
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
                            File f = new File(getPath(value));
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
                            File f = new File(getPath(value));
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
                            File f = new File(getPath(value));
                            if (f.isDirectory()) {
                                System.out.print("\033[32m" + value + "\033[0m" + str);
                            } else {
                                System.out.print(value + str);
                            }

                        }else {
                            File f = new File(getPath(value));
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
        } catch (Exception e){
            System.out.println("罗列文件（ls）指令失败");
            System.out.println(e);
        }
    }
    private void intoFile() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.print("请输入文件名：");
            String Filename = getPath(s.next());
            File f1 = new File(Filename);
            if (f1.exists()){
                if (f1.isDirectory()){
                    path = f1.getPath();
                }else {
                    System.out.println(Filename + "不是一个目录");
                }
            }else {
                System.out.println("没有找到" + Filename);
            }
        } catch (Exception e){
            System.out.println("进入文件夹（cd）指令失败");
            System.out.println(e);
        }
    }
    private void copyFile() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.print("请输入原文件名：");
            String oldPath = getPath(s.next());
            System.out.print("请输入新文件名：");
            String newPath = getPath(s.next());
            File f = new File(newPath);
            if (f.exists()){
                System.out.println("已有文件名，是否覆盖？");
                String a = s.next();
                if (a.equals("y") | a.equals("Y")){
                    copyFile(oldPath,newPath);
                }else {
                    System.out.println("已结束复制操作");
                }
            }else {
                copyFile(oldPath,newPath);
            }
        } catch (Exception e){
            System.out.println("复制文件（cp）指令失败");
            System.out.println(e);
        }
    }
    private void copyFile(String oldPath, String newPath) throws IOException {
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
    private void copyAllFile() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.print("请输入原文件名：");
            String oldPath = getPath(s.next());
            System.out.print("请输入新文件名：");
            String newPath = getPath(s.next());
            File f = new File(newPath);
            if (f.exists()){
                System.out.println("已有文件名，是否覆盖？");
                String a = s.next();
                if (a.equals("y") | a.equals("Y")){
                    copyAllFile(oldPath,newPath);
                }else {
                    System.out.println("已结束复制操作");
                }
            }else {
                copyAllFile(oldPath,newPath);
            }
        } catch (Exception e){
            System.out.println("复制文件夹（cpa）指令失败");
            System.out.println(e);
        }
    }
    private void copyAllFile(String oldPath,String newPath) throws IOException {
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
    private int getChineseNumber(String str) {
        Pattern p = Pattern.compile("[\u4E00-\u9FA5|！，。（）《》“”？：；【】]");
        Matcher m = p.matcher(str);
        int count = 0;
        while(m.find()){
            count ++;
        }
        return count - count/5;
    }
    private void saleFile() {
        try {
            Scanner s = new Scanner(System.in);
            System.out.print("请输入要加密的文件名：");
            String fPath = s.next();
            System.out.println("请注意，您正在使用加密功能，请确保您能记住密码以防文件无法恢复");
            System.out.println("请注意，我们没有保存您的密码，而且您的每一步操作都是不可逆的。");
            System.out.print("请输入密码（六位数字）：");
            int key = getInt();
            File file = new File(path + File.separator + fPath);
            if(file.exists()){
                String temp = saleFile(file.getPath(),key);
                new File(path + File.separator + temp).delete();
            } else {
                System.out.println("没有找到加密文件");
            }
        } catch (Exception e){
            System.out.println("加密文件（sl）指令失败");
            System.out.println(e);
        }
    }
    private String saleFile(String fPath,int key) throws IOException {
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
        return "temp";
    }
    private void resaleFile() {
        try {
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
                String temp = resaleFile(file.getPath(),key);
                new File(path + File.separator + temp).delete();
            } else {
                System.out.println("没有找到解密文件");
            }
        } catch (Exception e){
            System.out.println("解密文件（rsl）指令失败");
            System.out.println(e);
        }
    }
    private String resaleFile(String fPath,int key) throws IOException {
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
                System.out.println("解密" + oldPath);
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
        return "temp";
    }
    private int getInt(){
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
    private String getPath(String filePath){
        if (!pan0.matcher(filePath).lookingAt()){
            filePath = path + File.separator + filePath;
        }
        return filePath;
    }
}

