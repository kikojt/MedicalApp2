package estgoh.tam.fjtr.medicalapp2;

public class MedicamentoPerTime {

    private String hora;
    private Medicamento medicamento;

    public MedicamentoPerTime(String aHora, Medicamento m) {
        this.hora = aHora;
        this.medicamento = new Medicamento(m);
    }

    public String getHora() {
        return hora;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }
}
