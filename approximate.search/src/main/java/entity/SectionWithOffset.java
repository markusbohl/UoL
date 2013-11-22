package entity;

public interface SectionWithOffset {

	String getContent();

	int getLength();

	int getOffset();

	String getFirstNCharacters(int n);

	String getLasttNCharacters(int n);

}