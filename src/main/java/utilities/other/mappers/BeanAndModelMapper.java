package utilities.other.mappers;


public interface BeanAndModelMapper<B, M> {
    M fromBeanToModel(B bean); // Da Bean a Model
    B fromModelToBean(M model); // Da Model a Bean
}
