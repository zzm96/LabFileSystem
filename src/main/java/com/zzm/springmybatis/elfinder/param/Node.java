package com.zzm.springmybatis.elfinder.param;

public class Node {

    private String source = "fileSystem";
    private String alias = "file";
    private String path = "D:/cloudFile";
    private Boolean _default = true;
    private String locale;
    private Constraint constraint;

    {
        constraint = new Constraint();
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Boolean get_default() {
        return _default;
    }

    public void set_default(Boolean _default) {
        this._default = _default;
    }

    public String getLocale() {
        return locale;
    }

    public void setLocale(String locale) {
        this.locale = locale;
    }

    public Constraint getConstraint() {
        return constraint;
    }

    public void setConstraint(Constraint constraint) {
        this.constraint = constraint;
    }
}
