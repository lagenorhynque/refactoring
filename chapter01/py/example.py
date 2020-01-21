import locale
import math


def statement(invoice, plays):
    total_amount = 0
    volume_credits = 0
    result = f'Statement for {invoice["customer"]}\n'

    locale.setlocale(locale.LC_ALL, 'en_US.UTF-8')

    def fmt(val):
        return locale.currency(val, grouping=True)

    for perf in invoice['performances']:
        play = plays[perf['playID']]
        this_amount = 0

        if play['type'] == 'tragedy':
            this_amount = 40000
            if perf['audience'] > 30:
                this_amount += 1000 * (perf['audience'] - 30)
        elif play['type'] == 'comedy':
            this_amount = 30000
            if perf['audience'] > 20:
                this_amount += 10000 + 500 * (perf['audience'] - 20)
            this_amount += 300 * perf['audience']
        else:
            raise ValueError(f'Unknown type: {play["type"]}')

        # ボリューム特典のポイントを加算
        volume_credits += max(perf['audience'] - 30, 0)
        # 喜劇のときは10人につき、さらにポイントを加算
        if "comedy" == play['type']:
            volume_credits += math.floor(perf['audience'] / 5)
        # 注文の内訳を出力
        result += (f'  {play["name"]}: {fmt(this_amount / 100)} '
                   f'({perf["audience"]} seats)\n')
        total_amount += this_amount
    result += f'Amount owed is {fmt(total_amount / 100)}\n'
    result += f'You earned {volume_credits} credits\n'
    return result


plays = {
    'hamlet': {
        'name': "Hamlet",
        'type': "tragedy"
    },
    "as-like": {
        'name': "As You Like It",
        'type': "comedy"
    },
    'othello': {
        'name': "Othello",
        'type': "tragedy"
    }
}

invoices = [{
    'customer':
    "BigCo",
    'performances': [{
        'playID': "hamlet",
        'audience': 55
    }, {
        'playID': "as-like",
        'audience': 35
    }, {
        'playID': "othello",
        'audience': 40
    }]
}]


def main():
    for invoice in invoices:
        print(statement(invoice, plays))


if __name__ == '__main__':
    main()
