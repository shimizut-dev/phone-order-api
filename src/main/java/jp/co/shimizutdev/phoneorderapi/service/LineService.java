package jp.co.shimizutdev.phoneorderapi.service;

import jp.co.shimizutdev.phoneorderapi.entity.LineEntity;
import jp.co.shimizutdev.phoneorderapi.repository.LineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * 回線Service
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LineService {

    private final LineRepository lineRepository;

    /**
     * IDで回線を取得
     *
     * @param id 回線ID
     * @return 回線
     */
    public Optional<LineEntity> getLineById(UUID id) {
        return lineRepository.findById(id);
    }

    /**
     * 回線を検索
     *
     * @return 回線List
     */
    public List<LineEntity> searchLines() {
        return lineRepository.findAll();
    }

    /**
     * 回線を保存
     *
     * @param line 回線
     * @return 回線
     */
    @Transactional
    public LineEntity saveLine(LineEntity line) {
        return lineRepository.save(line);
    }
}
