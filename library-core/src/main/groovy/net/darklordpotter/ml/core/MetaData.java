package net.darklordpotter.ml.core;

/**
 * 2014-02-03
 *
 * @author Michael Rose <michael@fullcontact.com>
 */
public class MetaData {
    public FFNMetaData getFfn() {
        return ffn;
    }

    public void setFfn(FFNMetaData ffn) {
        this.ffn = ffn;
    }

    public DLPMetaData getDlp() {
        return dlp;
    }

    public void setDlp(DLPMetaData dlp) {
        this.dlp = dlp;
    }

    private FFNMetaData ffn;
    private DLPMetaData dlp;
}
