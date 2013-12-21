package common.preparation;

import java.io.BufferedReader;

import search.preparation.StringProvider;

import com.google.inject.AbstractModule;
import com.google.inject.assistedinject.FactoryModuleBuilder;

public class ReferenceSequenceModule extends AbstractModule {

	@Override
	protected void configure() {
		install(new FactoryModuleBuilder().implement(BufferedReader.class, BufferedFileReader.class).build(
				BufferedFileReaderFactory.class));
		install(new FactoryModuleBuilder().implement(StringProvider.class, FileContentProvider.class).build(
				StringProviderFactory.class));
	}
}
