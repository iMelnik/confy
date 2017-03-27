package de.codecentric.nbyl.confy.domain.speakers;

import de.codecentric.nbyl.confy.api.commands.speakers.CreateSpeakerCommand;
import de.codecentric.nbyl.confy.api.commands.speakers.DeleteSpeakerCommand;
import de.codecentric.nbyl.confy.api.commands.speakers.UpdateSpeakerCommand;
import de.codecentric.nbyl.confy.api.commands.talks.CreateTalkCommand;
import de.codecentric.nbyl.confy.api.commands.talks.DeleteTalkCommand;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.commandhandling.model.Repository;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SpeakerCommandHandler {

    private final Repository<Speaker> speakerRepository;

    public SpeakerCommandHandler(Repository<Speaker> speakerRepository) {
        this.speakerRepository = speakerRepository;
    }

    @CommandHandler
    public String createSpeaker(CreateSpeakerCommand command) throws Exception {
        String id = UUID.randomUUID().toString();
        speakerRepository.newInstance(() -> new Speaker(id, command.getSurname(), command.getFirstName()));
        return id;
    }

    @CommandHandler
    public void updateSpeaker(UpdateSpeakerCommand command) {
        speakerRepository.load(command.getId()).execute(speaker -> speaker.update(command));
    }

    @CommandHandler
    public void deleteSpeaker(DeleteSpeakerCommand command) {
        speakerRepository.load(command.getId()).execute(speaker -> speaker.delete());
    }

    @CommandHandler
    public String createTalk(CreateTalkCommand command) {
        String id = UUID.randomUUID().toString();
        speakerRepository.load(command.getSpeakerId()).execute(speaker -> speaker.addTalk(
                id,
                command.getTitle(),
                command.getEvent(),
                command.getDateHeld()
        ));
        return id;
    }

     @CommandHandler
    public void deleteTalk(DeleteTalkCommand command) {
        speakerRepository.load(command.getSpeakerId()).execute(speaker -> speaker.deleteTalk(command.getId()));
     }
}