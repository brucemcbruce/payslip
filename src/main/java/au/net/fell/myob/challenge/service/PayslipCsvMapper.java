package au.net.fell.myob.challenge.service;

import au.net.fell.myob.challenge.model.Payslip;
import au.net.fell.myob.challenge.model.PayslipRequest;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.stream.Collectors.toList;
import static org.eclipse.jetty.util.StringUtil.isNotBlank;

@Service
public class PayslipCsvMapper {

    private final PayslipFactory payslipFactory;
    private final CsvMapper csvMapper;

    private final CsvSchema inputSchema;
    private final CsvSchema outputSchema;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    public PayslipCsvMapper(PayslipFactory payslipFactory, CsvMapper csvMapper) {
        this.payslipFactory = payslipFactory;
        this.csvMapper = csvMapper;

        this.inputSchema = csvMapper.schemaFor(PayslipRequest.class);
        this.outputSchema = csvMapper.schemaFor(Payslip.class);
    }


    public String generatePayslipCsv(String request) {
        checkArgument(isNotBlank(request), "no CSV provided to map from!");

        List<Payslip> payslips = readCsv(request).stream().
                map(payslipFactory::generatePayslip).
                collect(toList());

        return writeCsv(payslips);
    }

    private List<PayslipRequest> readCsv(String request) {
        try {
            MappingIterator<PayslipRequest> mappingIterator = csvMapper.readerFor(PayslipRequest.class).
                    with(inputSchema.withHeader()).
                    readValues(request);
            List<PayslipRequest> requests = mappingIterator.readAll();
            logger.info("Mapping " + requests.size() + " requests");
            return requests;
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to read CSV provided", e);
        }
    }

    private String writeCsv(List<Payslip> payslips) {
        try {
            return csvMapper.writer(outputSchema.withHeader()).writeValueAsString(payslips);
        } catch (IOException e) {
            throw new IllegalArgumentException("unable to read CSV provided", e);
        }
    }

}
