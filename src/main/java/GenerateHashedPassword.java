import org.mindrot.jbcrypt.BCrypt;

public class GenerateHashedPassword {
    public static void main(String[] args) {
        String plainPassword = "adminpwd3";
        String hashed = BCrypt.hashpw(plainPassword, BCrypt.gensalt());
        System.out.println("加密後密碼：" + hashed);
    }
}
