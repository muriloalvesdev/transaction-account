package br.com.transactions.service;

import java.util.Optional;
import org.springframework.stereotype.Service;
import br.com.transactions.domain.model.SummarySale;
import br.com.transactions.domain.repository.SummarySaleRepository;
import br.com.transactions.resource.SummarySaleResource;
import br.com.transactions.service.exception.SummarySaleExistException;
import br.com.transactions.service.exception.SummarySaleNotFoundException;

@Service
public class SummarySaleServiceImpl implements SummarySaleService {

  private SummarySaleRepository summarySaleRepository;

  public SummarySaleServiceImpl(SummarySaleRepository summarySaleRepository) {
    this.summarySaleRepository = summarySaleRepository;
  }

  @Override
  public SummarySaleResource findByNumberSummarySale(String number)
      throws SummarySaleNotFoundException {

    SummarySale summarySale = summarySaleRepository.findByNumberSummarySale(Long.parseLong(number))
        .orElseThrow(() -> new SummarySaleNotFoundException(
            "Summary Sale not found through the number summary sale informed, number [" + number
                + "]"));

    return new SummarySaleResource(summarySale.getNetAmountSale(), summarySale.getGrossAmountSale(),
        summarySale.getMerchantDiscountRate(), summarySale.getNumberSummarySale());
  }

  @Override
  public SummarySale save(SummarySaleResource summarySaleDTO) throws SummarySaleExistException {
    Optional<SummarySale> optionalSummary =
        summarySaleRepository.findByNumberSummarySale(summarySaleDTO.getNumberSummarySale());
    if (optionalSummary.isPresent()) {
      throw new SummarySaleExistException("This summary number[ "
          + summarySaleDTO.getNumberSummarySale() + " ]already exists in the database!");
    } else {
      return summarySaleRepository.saveAndFlush(
          new SummarySale(summarySaleDTO.getNetAmountSale(), summarySaleDTO.getGrossAmountSale(),
              summarySaleDTO.getMerchantDiscountRate(), summarySaleDTO.getNumberSummarySale()));

    }
  }

}