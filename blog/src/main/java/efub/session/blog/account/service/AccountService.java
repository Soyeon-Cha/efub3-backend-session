package efub.session.blog.account.service;

import efub.session.blog.account.domain.Account;
import efub.session.blog.account.dto.request.AccountUpdateRequestDto;
import efub.session.blog.account.dto.request.SignUpRequestDto;
import efub.session.blog.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;

    public Long signUp(SignUpRequestDto requestDto){
        if (existsByEmail(requestDto.getEmail())){
            throw new IllegalArgumentException("이미 존재하는 email입니다. " + requestDto.getEmail());
        }
        Account account = accountRepository.save(requestDto.toEntity());
        return account.getAccountId();
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public boolean existsByEmail(String email){
        return accountRepository.existsByEmail(email);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("해당 id 를 가진 Account 를 찾을 수 없습니다. id ="+id));
    }

    public Long update(Long accountId, AccountUpdateRequestDto requestDto){
        Account account = findAccountById(accountId);
        account.updateAccount(requestDto.getBio(), requestDto.getNickname());
        return account.getAccountId();
    }

    @Transactional
    public void withdraw(Long accountId) {
        Account account = findAccountById(accountId);
        account.withdrawAccount();
    }

    public void delete(Long accountId) {
        Account account = findAccountById(accountId);
        accountRepository.delete(account);
    }

    @org.springframework.transaction.annotation.Transactional(readOnly = true)
    public Account findAccountByEmail(String email){ // follow 기능 때문에 추가됐음
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 email 를 가진 계정을 찾을 수 없습니다. email =" + email));
    }
}

