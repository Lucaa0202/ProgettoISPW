package patterns.factory;

import utilities.other.mappers.BeanAndModelMapper;

import java.util.HashMap;
import java.util.Map;

public class BeanAndModelMapperFactory {

    /************ Singleton ************/
    private static BeanAndModelMapperFactory instance = null;

    private BeanAndModelMapperFactory() {
    }

    public static BeanAndModelMapperFactory getInstance() {
        if (instance == null) {
            instance = new BeanAndModelMapperFactory();
        }
        return instance;
    }
    /*************************************/

    // Mappa per i mapper Bean -> Model
    private final Map<Class<?>, BeanAndModelMapper<?, ?>> beanToModelMappers = new HashMap<>();

    // Mappa per i mapper Model -> Bean
    private final Map<Class<?>, BeanAndModelMapper<?, ?>> modelToBeanMappers = new HashMap<>();

    // Registra un mapper per una coppia Bean-Model. <B> è il tipo del Bean, <M> è il tipo del Model.
    public <B, M> void registerMapper(Class<B> beanClass, Class<M> modelClass, BeanAndModelMapper<B, M> mapper) {
        if (beanToModelMappers.containsKey(beanClass) || modelToBeanMappers.containsKey(modelClass)) {
            throw new IllegalStateException("Mapper già registrato per: " + beanClass.getSimpleName() + " e " + modelClass.getSimpleName());
        }
        beanToModelMappers.put(beanClass, mapper);
        modelToBeanMappers.put(modelClass, mapper);
    }

    //Converte un Bean in un Model. Prende come parametri il bean da convertire e la classe del bean.
    @SuppressWarnings("unchecked")
    public <B, M> M fromBeanToModel(B bean, Class<B> beanClass) {
        BeanAndModelMapper<B, M> mapper = (BeanAndModelMapper<B, M>) beanToModelMappers.get(beanClass);
        if (mapper == null) {
            throw new IllegalArgumentException("Nessun mapper registrato per il Bean: " + beanClass.getSimpleName());
        }
        return mapper.fromBeanToModel(bean);
    }

    //Converte un Model in un Bean. Il metodo prende come parametri il model da convertire e la sua classe
    @SuppressWarnings("unchecked")
    public <B, M> B fromModelToBean(M model, Class<M> modelClass) {
        BeanAndModelMapper<B, M> mapper = (BeanAndModelMapper<B, M>) modelToBeanMappers.get(modelClass);
        if (mapper == null) {
            throw new IllegalArgumentException("Nessun mapper registrato per il Model: " + modelClass.getSimpleName());
        }
        return mapper.fromModelToBean(model);
    }
}