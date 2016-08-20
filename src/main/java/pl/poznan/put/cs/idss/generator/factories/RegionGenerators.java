package pl.poznan.put.cs.idss.generator.factories;

import pl.poznan.put.cs.idss.generator.settings.Coordinate;
import pl.poznan.put.cs.idss.generator.settings.ShapeType;
import pl.poznan.put.cs.idss.generator.generation.RegionGenerator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class RegionGenerators {

    private final Map<Integer, List<RegionGenerator>> _regionGenerators = new HashMap<>();
    private Coordinate _minCoordinate = null;
    private Coordinate _maxCoordinate = null;

    public RegionGenerators(List<RegionDescription> regionDescriptions) {
        List<RegionDescription> integumentalRegions = new ArrayList<>();
        List<RegionGenerator> otherGenerators = new ArrayList<>();

        for (RegionDescription descr : regionDescriptions) {
            if (descr.getRegion().getShape() == ShapeType.INTEGUMENTAL) {
                integumentalRegions.add(descr);
            } else {
                RegionGenerator generator = RegionGeneratorFactory.createNonIntegumentalRegionGenerator(descr);
                addRegionGenerator(descr.getClassIndex(), generator);
                otherGenerators.add(generator);
            }
        }

        for (RegionDescription descr : integumentalRegions) {
            addRegionGenerator(descr.getClassIndex(), RegionGeneratorFactory.createIntegumentalRegionGenerator(descr, otherGenerators));
        }

        updateCoordinates(regionDescriptions);
    }

    private void addRegionGenerator(int classIndex, RegionGenerator regionGenerator) {
        if (!_regionGenerators.containsKey(classIndex)) {
            _regionGenerators.put(classIndex, new ArrayList<>());
        }
        _regionGenerators.get(classIndex).add(regionGenerator);
    }

    public Map<Integer, List<RegionGenerator>> getRegionGenerators() {
        return _regionGenerators;
    }

    private void updateCoordinates(List<RegionDescription> descriptions) {
        if (descriptions.isEmpty()) 
            return;
        _minCoordinate = new Coordinate(descriptions.get(0).getRegion().getCenter());
        _maxCoordinate = new Coordinate(_minCoordinate);
        for (RegionDescription descr : descriptions) {
            for (int i = 0; i < _maxCoordinate.getNumDimensions(); i++) {
                double max = descr.getRegion().getCenter().get(i) + descr.getRegion().getBorderRadius().get(i);
                double min = descr.getRegion().getCenter().get(i) - descr.getRegion().getBorderRadius().get(i);
                _maxCoordinate.set(i, Math.max(_maxCoordinate.get(i), max));
                _minCoordinate.set(i, Math.min(_minCoordinate.get(i), min));
            }
        }
    }

    public Coordinate getMinCoordinate() {
        return _minCoordinate;
    }

    public Coordinate getMaxCoordinate() {
        return _maxCoordinate;
    }
}
