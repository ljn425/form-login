package security.formlogin.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import security.formlogin.domain.entity.Resources;
import security.formlogin.repository.ResourcesRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResourcesService {

    private final ResourcesRepository resourcesRepository;

    public Resources getResources(long id) {
        return resourcesRepository.findById(id).orElse(new Resources());
    }

    public List<Resources> getResources() {
        return resourcesRepository.findAll(Sort.by(Sort.Order.asc("orderNum")));
    }
    @Transactional
    public void createResources(Resources resources) {
        resourcesRepository.save(resources);
    }
    @Transactional
    public void deleteResources(long id) {
        resourcesRepository.deleteById(id);
    }

}
