package estgoh.tam.fjtr.medicalapp2;


public class Medicamento {
    private int id;
    private String nome;
    private String dosagem;
    private String formaFarmaceutica;
    private String posologia;
    private String hora1;
    private String hora2;
    private String hora3;
    private String hora4;
    private int quantidade;
    private String duracao;
    private String dataInicio;
    private int ativo;

    public Medicamento(int aId, String aNome, String aDosagem, String aFormaFarmaceutica, String aPosologia, String aHora1, String aHora2, String aHora3, String aHora4, int aQuantidade, String aDuracao, String aDataInicio, int aAtivo) {
        this.id = aId;
        this.nome = aNome;
        this.dosagem = aDosagem;
        this.formaFarmaceutica = aFormaFarmaceutica;
        this.posologia = aPosologia;
        this.hora1 = aHora1;
        this.hora2 = aHora2;
        this.hora3 = aHora3;
        this.hora4 = aHora4;
        this.quantidade = aQuantidade;
        this.duracao = aDuracao;
        this.dataInicio = aDataInicio;
    }

    public int getId() {
        return id;
    }
    public String getNome() {
        return nome;
    }

    public String getDosagem() {
        return dosagem;
    }

    public String getFormaFarmaceutica() {
        return formaFarmaceutica;
    }

    public String getPosologia() {
        return posologia;
    }

    public String getHora1() {
        return hora1;
    }

    public String getHora2() {
        return hora2;
    }

    public String getHora3() {
        return hora3;
    }

    public String getHora4() {
        return hora4;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public String getDuracao() {
        return duracao;
    }

    public String getDataInicio() {
        return dataInicio;
    }

    public int getAtivo() {
        return ativo;
    }

    public void setNome(String aNome) {
        this.nome = aNome;
    }

    public void setDosagem(String aDosagem) {
        this.dosagem = aDosagem;
    }

    public void setFormaFarmaceutica(String aFormaFarmaceutica) {
        this.formaFarmaceutica = aFormaFarmaceutica;
    }

    public void setPosologia(String aPosologia) {
        this.posologia = aPosologia;
    }

    public void setHora1(String hora1) {
        this.hora1 = hora1;
    }

    public void setHora2(String hora2) {
        this.hora2 = hora2;
    }

    public void setHora3(String hora3) {
        this.hora3 = hora3;
    }

    public void setHora4(String hora4) {
        this.hora4 = hora4;
    }

    public void setQuantidade(int aQuantidade) {
        this.quantidade = aQuantidade;
    }

    public void setDuracao(String aDuracao) {
        this.duracao = aDuracao;
    }

    public void setDataInicio(String aDataInicio) {
        this.dataInicio = aDataInicio;
    }

    public void setAtivo(int aAtivo) {
        this.ativo = aAtivo;
    }
}
