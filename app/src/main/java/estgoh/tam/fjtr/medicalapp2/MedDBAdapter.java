package estgoh.tam.fjtr.medicalapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MedDBAdapter {
    String TAG = "MedicalApp";
    String DB_NAME = "BD_Med";
    String DB_TABLE = "medicamento";
    int DB_VERSION = 1;

    String SQL_CREATE = "CREATE TABLE " + DB_TABLE +
            "(id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "nome TEXT NOT NULL, " +
            "dosagem TEXT NOT NULL, " +
            "formaFarmaceutica TEXT, " +
            "posologia TEXT, " +
            "hora1 TEXT, " +
            "hora2 TEXT, " +
            "hora3 TEXT, " +
            "hora4 TEXT, " +
            "quantidade INTEGER, " +
            "duracao TEXT, " +
            "dataInicio TEXT, " +
            "ativo INTEGER NOT NULL CHECK (ativo IN (0, 1)))";

    String SQL_DROP = "DROP TABLE IF EXISTS " + DB_TABLE;

    DatabaseHelper myDb;
    private SQLiteDatabase db;

    public MedDBAdapter(Context context){
        myDb = new DatabaseHelper(context);
    }

    class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context){
            super(context, DB_NAME, null, DB_VERSION);
        }
        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE);
            Log.d(TAG, "Tabela de medicamentos criada!");
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DROP);
            onCreate(db);
            Log.d(TAG, "Tabela de medicamentos recriada!");
        }
    }

    public void open() throws SQLException {
        db = myDb.getWritableDatabase();
    }

    public void close(){
        myDb.close();
    }

    public long adicionarMedicamento(String nome, String dosagem, String formaFarmaceutica, String posologia, String hora1, String hora2, String hora3, String hora4, int quantidade, String duracao, String dataInicio) {
        // Verifica quantos id´s existem
        String query = "SELECT MAX(id) FROM " + DB_TABLE;
        Cursor cursor = db.rawQuery(query, null);

        long maxId = 0;
        if (cursor != null && cursor.moveToFirst()) {
            maxId = cursor.getLong(0);
            cursor.close();
        }
        long newId = maxId + 1;

        ContentValues values = new ContentValues();
        values.put("id", newId);
        values.put("nome", nome);
        values.put("dosagem", dosagem);
        values.put("formaFarmaceutica", formaFarmaceutica);
        values.put("posologia", posologia);
        values.put("hora1", hora1);
        values.put("hora2", hora2);
        values.put("hora3", hora3);
        values.put("hora4", hora4);
        values.put("quantidade", quantidade);
        values.put("duracao", duracao);
        values.put("dataInicio", dataInicio);
        values.put("ativo", 0);

        return db.insert(DB_TABLE,
                         null,
                         values);
    }

    public int editarMedicamento(int id, String nome, String dosagem, String formaFarmaceutica, String posologia, String hora1, String hora2, String hora3, String hora4, int quantidade, String duracao, String dataInicio) {
        ContentValues values = new ContentValues();
        values.put("nome", nome);
        values.put("dosagem", dosagem);
        values.put("formaFarmaceutica", formaFarmaceutica);
        values.put("posologia", posologia);
        values.put("hora1", hora1);
        values.put("hora2", hora2);
        values.put("hora3", hora3);
        values.put("hora4", hora4);
        values.put("quantidade", quantidade);
        values.put("duracao", duracao);
        values.put("dataInicio", dataInicio);

        return db.update(DB_TABLE,
                         values,
                         "id=?",
                         new String[]{String.valueOf((id))});
    }

    public Cursor obterTodosMedicamentos() {
        Cursor cursor = db.query(DB_TABLE,
                                 new String[]{"id", "nome", "dosagem", "formaFarmaceutica", "posologia", "hora1", "hora2", "hora3", "hora4", "quantidade", "duracao", "dataInicio", "ativo"},
                                 null,
                                 null,
                                 null,
                                 null,
                                 "dataInicio DESC");
        return cursor;
    }

    public Cursor obterMedicamento(long id) {
        Cursor cursor = db.query(DB_TABLE,
                new String[]{"id", "nome", "dosagem", "formaFarmaceutica", "posologia", "hora1", "hora2", "hora3", "hora4", "quantidade", "duracao", "dataInicio", "ativo"},
                "id=?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null);
        return cursor;
    }

    public void removerMedicamento(long id) {
        // Remove a linha (Medicamento) com o id fornecido
        db.delete(DB_TABLE,
                  "id=?",
                  new String[]{String.valueOf(id)});

        // Faz a redefinição dos id's na tabela
        String updateQuery = "UPDATE " + DB_TABLE + " SET id = id - 1 WHERE id > ?";
        db.execSQL(updateQuery, new String[]{String.valueOf(id)});
    }

    // Verifica se o medicamento já existe através do nome e data de início
    public boolean verificarExistenciaMedicamento(String nome, String dataInicio) {
        Cursor cursor = db.query(DB_TABLE,
                                 new String[]{"id"},
                                 "nome=? AND dataInicio=?",
                                 new String[]{nome, dataInicio},
                                 null,
                                 null,
                                 null
        );

        boolean existeMedicamento = cursor.getCount() > 0;
        cursor.close();
        return existeMedicamento;
    }

    // Verifica se o medicamento já existe através do nome e data de início,
    // excluindo o medicamento atual, ataravés do id
    public boolean verificarExistenciaMedEdicao(int id, String nome, String dataInicio) {
        Cursor cursor = db.query(DB_TABLE,
                                 new String[]{"id"},
                                 "id!=? AND nome=? AND dataInicio=?",
                                 new String[]{ String.valueOf(id), nome, dataInicio},
                                 null,
                                 null,
                                 null
        );

        boolean existeMedicamento = cursor.getCount() > 0;
        cursor.close();
        return existeMedicamento;
    }

    // Ativa o medicamento na qual esse se encontra dentro do periodo de administração
    public int atualizarAtivoMedicamento(long id, int novoAtivo) {
        ContentValues values = new ContentValues();
        values.put("ativo", novoAtivo);

        return db.update(DB_TABLE,
                         values,
                         "id=?",
                         new String[]{String.valueOf(id)});
    }

    // Obtém os medicamentos ativos, que estão a ser administrados no dia de hoje
    public Cursor obterMedicamentosAtivos() {
        Cursor cursor = db.query(DB_TABLE,
                new String[]{"id", "nome", "dosagem", "formaFarmaceutica", "posologia", "hora1", "hora2", "hora3", "hora4", "quantidade", "duracao", "dataInicio", "ativo"},
                "ativo=?",
                new String[]{String.valueOf(1)}, // 1 indica medicamentos ativos
                null,
                null,
                "hora1 ASC, hora2 ASC, hora3 ASC, hora4 ASC");

        return cursor;
    }
}
