package ru.otus.serialization.services.impl;

import org.springframework.stereotype.Service;
import ru.otus.serialization.dtos.MessageDto;
import ru.otus.serialization.dtos.SmsStatisticsDto;
import ru.otus.serialization.entities.ChatSession;
import ru.otus.serialization.entities.Member;
import ru.otus.serialization.entities.Message;
import ru.otus.serialization.repositories.SmsRepository;
import ru.otus.serialization.services.SmsService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SmsServiceImpl implements SmsService {

    private final SmsRepository smsRepository;

    public SmsServiceImpl(SmsRepository smsRepository) {
        this.smsRepository = smsRepository;
    }

    public Map<String, SmsStatisticsDto> getStatistics() {
        Map<String, SmsStatisticsDto> smsInfoStatistics = new HashMap<>();
        for (ChatSession chatSession : smsRepository.getSmsInfo().getChatSessions()) {
            for (Message message : chatSession.getMessages()) {
                SmsStatisticsDto statistics;
                if (smsInfoStatistics.containsKey(message.getBelongNumber())) {
                    statistics = smsInfoStatistics.get(message.getBelongNumber());
                } else {
                    statistics = new SmsStatisticsDto();
                    statistics.setChatIdentifier(chatSession.getChatIdentifier());
                    statistics.setLastList(chatSession.getMembers()
                            .stream()
                            .map(Member::getLast)
                            .collect(Collectors.toList()));
                }

                statistics.getMessages().add(new MessageDto(
                        message.getBelongNumber(),
                        message.getSendDate(), message.getText())
                );

                smsInfoStatistics.put(message.getBelongNumber(), statistics);
            }
        }

        for (SmsStatisticsDto smsInfoDto : smsInfoStatistics.values()) {
            smsInfoDto.getMessages().sort((o1, o2) -> {
                try {
                    return SmsServiceImpl.toDate(o1.getSendDate()).compareTo(SmsServiceImpl.toDate(o2.getSendDate()));
                } catch (ParseException e) {
                    return 0;
                }
            });
        }
        return smsInfoStatistics;
    }

    static private Date toDate(String value) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yy HH:mm:ss");
        return dateFormat.parse(value);
    }
}

