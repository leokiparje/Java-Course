package hr.fer.oprpp1.hw04.db;

/**
 * Class FieldValueGetters represents field value getters
 * @author leokiparje
 *
 */

public class FieldValueGetters {

	public final static IFieldValueGetter FIRST_NAME = (record) -> record.getFirstName();
	public final static IFieldValueGetter LAST_NAME = (record) -> record.getLastName();
	public final static IFieldValueGetter JMBAG = (record) -> record.getJmbag();
	
}
