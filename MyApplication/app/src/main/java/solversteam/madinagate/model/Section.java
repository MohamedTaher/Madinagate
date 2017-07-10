package solversteam.madinagate.model;

import android.content.Intent;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by taher on 4/25/17.
 */

public class Section implements Serializable{
    private String title;
    private ArrayList<String> subTilte;
    private ArrayList<Integer> categoryIDs;

    public Section(String title, ArrayList<String> subTilte, ArrayList<Integer> categoryIDs) {
        this.title = title;
        this.subTilte = subTilte;
        this.categoryIDs = categoryIDs;
    }

    public String getTitle() {
        return title.split("-")[1];
    }

    public String getID() {
        return title.split("-")[0];
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<String> getSubTilte() {
        return subTilte;
    }

    public void setSubTilte(ArrayList<String> subTilte) {
        this.subTilte = subTilte;
    }

    public ArrayList<Integer> getCategoryIDs() {
        return categoryIDs;
    }

    public void setCategoryIDs(ArrayList<Integer> categoryIDs) {
        this.categoryIDs = categoryIDs;
    }
}
