package com.raambalan.mastercard;

import com.raambalan.mastercard.model.City;
import com.raambalan.mastercard.model.CityPathGraph;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import static com.raambalan.mastercard.model.Graph.ResultType;

@Slf4j
@Service
public class CityConnectService implements CityConnectQuery {
    private CityConnectRepository repository;
    private CityPathGraph pathMap;

    public CityConnectService(CityConnectRepository repository) {
        this.repository = repository;

    }

    @Override
    public ResultType findPath(String source, String destination) {
        Queue<String> queue = new LinkedList<>();
        Set<String> visited = new HashSet<>();

        if (pathMap.getCity(source)
                .getName().equalsIgnoreCase("CityNotfound"))
            return ResultType.SOURCE_NOT_FOUND;

        if (pathMap.getCity(destination)
                .getName().equalsIgnoreCase("CityNotfound"))
            return ResultType.DEST_NOT_FOUND;

        queue.add(source);
        while (!queue.isEmpty()) {
            String currentCity = queue.poll();
            visited.add(currentCity);
            if (currentCity.equalsIgnoreCase(destination))
                return ResultType.PATH_FOUND;
            City city = pathMap.getCity(currentCity);
            for (String name : city.getAdjacentCities()) {
                if (!visited.contains(name)) {
                    queue.add(name);
                }
            }
        }
        return ResultType.NO_PATH_FOUND;
    }

    @Override
    public void loadPaths(String fileName) throws IOException {
        this.pathMap = repository.loadPaths(fileName);
    }
}
