package z.bank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import z.bank.entity.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {}
