import example as sut


def test_statement():
    assert [sut.statement(invoice, sut.plays) for invoice in sut.invoices] == [
        '''Statement for BigCo
  Hamlet: $650.00 (55 seats)
  As You Like It: $580.00 (35 seats)
  Othello: $500.00 (40 seats)
Amount owed is $1,730.00
You earned 47 credits
'''
    ]
