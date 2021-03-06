package top.dddpeter.ishare.job.client.model;

import java.util.Objects;

/**
 * @author hqins 2019-12-10
 */
public class RegistryDataParamVO {


    private String key;
    private String value;


    public RegistryDataParamVO() {
    }
    public RegistryDataParamVO(String key, String value) {
        this.key = key;
        this.value = value;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        RegistryDataParamVO that = (RegistryDataParamVO) o;
        return Objects.equals(key, that.key) &&
                Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "XxlRegistryDataParamVO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

}
