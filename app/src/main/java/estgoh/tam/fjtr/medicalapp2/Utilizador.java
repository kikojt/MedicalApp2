package estgoh.tam.fjtr.medicalapp2;

public class Utilizador {
    private int u_id;
    private String u_nome;
    private String u_password;

    public String getU_token() {
        return u_token;
    }

    public void setU_token(String u_token) {
        this.u_token = u_token;
    }
    private String u_token;

    public int getU_id() {
        return u_id;
    }

    public String getNome() {
        return u_nome;
    }

    public String getU_password() {
        return u_password;
    }

    public void setId(int u_id) {
        this.u_id = u_id;
    }

    public void setU_nome(String u_nome) {
        this.u_nome = u_nome;
    }

    public void setU_password(String u_password) {
        this.u_password = u_password;
    }
}
