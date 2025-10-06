package sc.snicky.springbootjwtauth.api.v1.domain.models;

public interface BaseModel<ID> {
    /**
     * Gets the identifier of the model.
     *
     * @return the identifier
     */
    ID getId();

    /**
     * Sets the identifier of the model.
     *
     * @param id the identifier
     */
    void setId(ID id);
}
