package tn.esprit.autoloc.service;

public interface IGenericService<T, I>
        extends IReadGenericService<T, I>,
        IWriteGenericService<T, I>,
        ICheckGenericService<T> {
}