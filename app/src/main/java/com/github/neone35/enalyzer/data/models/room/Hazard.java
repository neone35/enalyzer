package com.github.neone35.enalyzer.data.models.room;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.List;

@Entity(tableName = "hazards")
public class Hazard {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "statement_code")
    private String statementCode;
    private String statement;

    // Constructor used by Room to create Hazards
    public Hazard(@NonNull String statementCode, String statement) {
        this.statementCode = statementCode;
        this.statement = statement;
    }

    @NonNull
    public String getStatementCode() {
        return statementCode;
    }

    public void setStatementCode(@NonNull String statementCode) {
        this.statementCode = statementCode;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}
