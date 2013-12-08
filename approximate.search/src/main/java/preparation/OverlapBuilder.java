package preparation;

import java.util.List;

import entity.SectionWithOffset;

public interface OverlapBuilder {

	List<SectionWithOffset> getOverlappingAreas();

	void feed(List<SectionWithOffset> sequenceSections, int patternLength, int allowedErrors);

}