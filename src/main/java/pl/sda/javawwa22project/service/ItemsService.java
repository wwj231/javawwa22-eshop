package pl.sda.javawwa22project.service;

//import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pl.sda.javawwa22project.respository.ItemsRepository;

//@Slf4j
@Service
public class ItemsService {

  private static final Logger logger = LoggerFactory.getLogger(ItemsService.class);
  private final ItemsRepository itemsRepository;

  public ItemsService(final ItemsRepository itemsRepository) {
    this.itemsRepository = itemsRepository;
//    log.
    logger.debug("object ItemsService created - debug level");
    logger.info("object ItemsService created - info level");
    // trace
    // debug
    // info
    // warn
    // error
  }
}
