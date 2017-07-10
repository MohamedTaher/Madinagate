package solversteam.madinagate.model;

import java.io.Serializable;
import java.lang.ref.PhantomReference;

/**
 * Created by root on 4/30/17.
 */

public class Post implements Serializable{

    private int id;
    private String title;
    private String introtext;
    private String fulltext;
    private int posttype;

    public Post(int id, String title, String introtext) {
        this.id = id;
        this.title = title;
        this.introtext = introtext;
    }

    public Post(int id, String title, String introtext, String fulltext) {
        this.id = id;
        this.title = title;
        this.introtext = introtext;
        this.fulltext = fulltext;
    }

    public Post(int id, String title, String introtext, String fulltext, int posttype) {
        this.id = id;
        this.title = title;
        this.introtext = introtext;
        this.fulltext = fulltext;
        this.posttype = posttype;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIntrotext() {
        return introtext;
    }

    public void setIntrotext(String introtext) {
        this.introtext = introtext;
    }

    public String getFulltext() {
        return fulltext;
    }

    public void setFulltext(String fulltext) {
        this.fulltext = fulltext;
    }

    public int getPosttype() {
        return posttype;
    }

    public void setPosttype(int posttype) {
        this.posttype = posttype;
    }
}
