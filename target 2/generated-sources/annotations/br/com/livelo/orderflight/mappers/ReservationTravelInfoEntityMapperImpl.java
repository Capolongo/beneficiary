package br.com.livelo.orderflight.mappers;

import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationDocument;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationPax;
import br.com.livelo.orderflight.domain.dto.reservation.request.ReservationRequest;
import br.com.livelo.orderflight.domain.dto.reservation.response.PartnerReservationTravelInfo;
import br.com.livelo.orderflight.domain.entity.DocumentEntity;
import br.com.livelo.orderflight.domain.entity.PaxEntity;
import br.com.livelo.orderflight.domain.entity.TravelInfoEntity;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.1 (Oracle Corporation)"
)
@Component
public class ReservationTravelInfoEntityMapperImpl implements ReservationTravelInfoEntityMapper {

    @Override
    public TravelInfoEntity toReservationTravelInfoEntity(ReservationRequest reservationRequest, PartnerReservationTravelInfo partnerReservationTravelInfo) {
        if ( reservationRequest == null && partnerReservationTravelInfo == null ) {
            return null;
        }

        TravelInfoEntity.TravelInfoEntityBuilder travelInfoEntity = TravelInfoEntity.builder();

        if ( reservationRequest != null ) {
            travelInfoEntity.paxs( reservationPaxListToPaxEntitySet( reservationRequest.getPaxs() ) );
        }
        if ( partnerReservationTravelInfo != null ) {
            travelInfoEntity.type( partnerReservationTravelInfo.getType() );
            travelInfoEntity.reservationCode( partnerReservationTravelInfo.getReservationCode() );
            travelInfoEntity.adt( partnerReservationTravelInfo.getAdt() );
            travelInfoEntity.chd( partnerReservationTravelInfo.getChd() );
            travelInfoEntity.inf( partnerReservationTravelInfo.getInf() );
            travelInfoEntity.voucher( partnerReservationTravelInfo.getVoucher() );
            travelInfoEntity.cabinClass( partnerReservationTravelInfo.getCabinClass() );
        }

        return travelInfoEntity.build();
    }

    protected DocumentEntity reservationDocumentToDocumentEntity(ReservationDocument reservationDocument) {
        if ( reservationDocument == null ) {
            return null;
        }

        DocumentEntity.DocumentEntityBuilder<?, ?> documentEntity = DocumentEntity.builder();

        documentEntity.documentNumber( reservationDocument.getDocumentNumber() );
        documentEntity.type( reservationDocument.getType() );
        documentEntity.issueDate( reservationDocument.getIssueDate() );
        documentEntity.issuingCountry( reservationDocument.getIssuingCountry() );
        documentEntity.expirationDate( reservationDocument.getExpirationDate() );
        documentEntity.residenceCountry( reservationDocument.getResidenceCountry() );

        return documentEntity.build();
    }

    protected Set<DocumentEntity> reservationDocumentListToDocumentEntitySet(List<ReservationDocument> list) {
        if ( list == null ) {
            return null;
        }

        Set<DocumentEntity> set = new LinkedHashSet<DocumentEntity>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ReservationDocument reservationDocument : list ) {
            set.add( reservationDocumentToDocumentEntity( reservationDocument ) );
        }

        return set;
    }

    protected PaxEntity reservationPaxToPaxEntity(ReservationPax reservationPax) {
        if ( reservationPax == null ) {
            return null;
        }

        PaxEntity.PaxEntityBuilder<?, ?> paxEntity = PaxEntity.builder();

        paxEntity.type( reservationPax.getType() );
        paxEntity.firstName( reservationPax.getFirstName() );
        paxEntity.lastName( reservationPax.getLastName() );
        paxEntity.email( reservationPax.getEmail() );
        paxEntity.areaCode( reservationPax.getAreaCode() );
        paxEntity.phoneNumber( reservationPax.getPhoneNumber() );
        paxEntity.gender( reservationPax.getGender() );
        paxEntity.birthDate( reservationPax.getBirthDate() );
        paxEntity.documents( reservationDocumentListToDocumentEntitySet( reservationPax.getDocuments() ) );

        return paxEntity.build();
    }

    protected Set<PaxEntity> reservationPaxListToPaxEntitySet(List<ReservationPax> list) {
        if ( list == null ) {
            return null;
        }

        Set<PaxEntity> set = new LinkedHashSet<PaxEntity>( Math.max( (int) ( list.size() / .75f ) + 1, 16 ) );
        for ( ReservationPax reservationPax : list ) {
            set.add( reservationPaxToPaxEntity( reservationPax ) );
        }

        return set;
    }
}
