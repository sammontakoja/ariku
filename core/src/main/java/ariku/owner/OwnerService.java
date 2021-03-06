package ariku.owner;

import ariku.database.CompetitionRepository;
import ariku.database.OwnerRecordRepository;
import ariku.user.UserService;
import ariku.util.AuthorizeRequest;
import ariku.util.Competition;
import ariku.database.CompetitionStateRepository;
import ariku.verification.UserVerificationService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Ari Aaltonen
 */
public class OwnerService {

    public CompetitionRepository competitionRepository;
    public OwnerRecordRepository ownerRecordRepository;
    public UserService userService;
    public UserVerificationService userVerificationService;
    public CompetitionStateRepository competitionStateRepository;

    public Optional<Competition> createNewCompetition(NewCompetitionRequest newCompetitionRequest) {

        String userIdOfAuthorizedUser = userVerificationService.userIdOfAuthorizedUser(newCompetitionRequest.authorizeRequest);
        if (userIdOfAuthorizedUser.isEmpty())
            return Optional.empty();

        if (newCompetitionRequest.competitionName.length() < 2 || newCompetitionRequest.competitionType.length() < 2)
            return Optional.empty();

        Competition competition = new Competition();
        competition.setId(competitionRepository.uniqueId());
        competition.setName(newCompetitionRequest.competitionName);
        competition.setType(newCompetitionRequest.competitionType);
        competitionRepository.store(competition);

        OwnerRecord ownerRecord = new OwnerRecord();
        ownerRecord.setUserId(userIdOfAuthorizedUser);
        ownerRecord.setCompetitionId(competition.getId());
        ownerRecordRepository.store(ownerRecord);

        return Optional.of(competition);
    }

    public List<Competition> findOwnedCompetitions(AuthorizeRequest request) {

        String userIdOfAuthorizedUser = userVerificationService.userIdOfAuthorizedUser(request);

        if (!userIdOfAuthorizedUser.isEmpty()) {

            List<OwnerRecord> usersOwnerRecords = ownerRecordRepository.listByUserId(userIdOfAuthorizedUser);

            return usersOwnerRecords.stream()
                    .map(ownerRecord -> competitionRepository.get(ownerRecord.getCompetitionId()).get())
                    .collect(Collectors.toList());
        }

        return new ArrayList<>();
    }

    public void addNewOwner(String userNameOfNewOwner, String competitionId, AuthorizeRequest authorizeRequest) {

        String userIdOfAuthorizedUser = userVerificationService.userIdOfAuthorizedUser(authorizeRequest);

        if (userIdOfAuthorizedUser.isEmpty())
            return;

        boolean userAddingOwnershipIsAlsoOwner = ownerRecordRepository.listByCompetitionId(competitionId).stream()
                .anyMatch(o -> o.getUserId().equals(userIdOfAuthorizedUser));

        if (!userAddingOwnershipIsAlsoOwner)
            return;

        userService.findUserByUsername(userNameOfNewOwner).ifPresent(userGoingToBeNewOwner -> {
            OwnerRecord record = new OwnerRecord();
            record.setCompetitionId(competitionId);
            record.setUserId(userGoingToBeNewOwner.getId());
            ownerRecordRepository.store(record);
        });

    }
}
