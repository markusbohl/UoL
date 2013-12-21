package common.preparation;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.inject.Inject;

import com.google.inject.assistedinject.Assisted;

public class BufferedFileReader extends BufferedReader {

	@Inject
	BufferedFileReader(@Assisted final String filePath) throws FileNotFoundException {
		super(new FileReader(filePath));
	}
}
