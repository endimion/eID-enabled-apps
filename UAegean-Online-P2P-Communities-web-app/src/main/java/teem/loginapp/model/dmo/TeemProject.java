/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package teem.loginapp.model.dmo;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 *
 * @author nikos
 */
@Document(collection = "models")
public class TeemProject {

    public static class Root {

        private String id;

        private String promoter;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPromoter() {
            return promoter;
        }

        public void setPromoter(String promoter) {
            this.promoter = promoter;
        }

    }

    @Id
    private String id;
    @Field("wave_id")
    private String waveId;
    @Field("wavelet_id")
    private String waveletId;
    private Root root;

    private List<String> participants;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWaveId() {
        return waveId;
    }

    public void setWaveId(String waveId) {
        this.waveId = waveId;
    }

    public String getWaveletId() {
        return waveletId;
    }

    public void setWaveletId(String waveletId) {
        this.waveletId = waveletId;
    }

    public List<String> getParticipants() {
        return participants;
    }

    public void setParticipants(List<String> participants) {
        this.participants = participants;
    }

    public Root getRoot() {
        return root;
    }

    public void setRoot(Root root) {
        this.root = root;
    }

}
