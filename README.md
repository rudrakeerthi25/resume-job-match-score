# Job Application Follow-Up Tracker

Final project for the Building AI course

## Summary

A simple tool that looks at your job applications and tells you which ones you should follow up on today, based on how long it's been since you last heard from them. No more forgetting to chase up a recruiter. Building AI course project.

## Background

If you've ever applied to more than a handful of jobs at once, you know the problem: you lose track. Some recruiters never reply, some take weeks, and it's easy to forget who you're still waiting on and who you should be chasing.

* It's hard to remember exactly how many days it's been since you emailed a recruiter
* Different stages need different urgency - waiting a week after just applying is fine, but waiting a week after an interview is not
* Most people use a messy spreadsheet or sticky notes, which they forget to check

My personal motivation: I'm actively job hunting right now, juggling multiple applications across different companies and stages, and I built this because I genuinely needed it.

## How is it used?

You keep a simple spreadsheet-style file (a CSV file) listing your job applications - the company, the role, what stage it's at, the date you last heard from them, and who your contact is.

Every day (or whenever you want a check-in), you run the tool, and it reads through your list and tells you in plain English exactly who to follow up with and why.

Example of what the tool prints out:

```
Job Application Follow-Up Reminders
----------------------------------------
Follow up with Don about the Senior Software Engineer role at Tribal Group - it's been 4 days since your last contact (current stage: interviewed).
Follow up with the recruiter about the Integration Engineer role at BildGroup - it's been 8 days since your last contact (current stage: applied).
```

This is meant for **job seekers** like myself, who are managing several live applications at once and want a simple daily nudge instead of relying on memory.

## Data sources and AI methods

This tool doesn't need any external dataset - it works entirely from your own personal job application data, which you keep in a simple CSV file on your own computer. Nothing is sent anywhere.

**The "intelligence" here is a rule-based decision system** - a simple but genuinely useful form of AI logic. Instead of treating every application the same way, the tool applies a different follow-up rule depending on the stage of the application:

| Stage | Follow up after |
| ----- | ---------------- |
| Applied | 7 days |
| Interviewed | 3 days |
| Offer pending | 2 days |

This mirrors how a thoughtful person would actually prioritise their own follow-ups - urgency increases the further along you are in the process.

```python
FOLLOW_UP_RULES = {
    "applied": 7,       # follow up after 7 days of silence
    "interviewed": 3,   # follow up sooner, after 3 days
    "offer": 2           # follow up very soon, after 2 days
}

def needs_follow_up(application):
    stage = application["stage"].strip().lower()
    last_contact = application["last_contact_date"].strip()

    if stage not in FOLLOW_UP_RULES:
        return False

    days_waited = days_since_contact(last_contact)
    threshold = FOLLOW_UP_RULES[stage]

    return days_waited >= threshold
```

## Challenges

This is a simple first version, so it has clear limitations:

* It only works with the rules I've set - it doesn't learn or adjust to your personal follow-up style over time
* It doesn't actually send the follow-up message for you - you still have to write and send it yourself
* It relies on you remembering to update the CSV file whenever something changes (a missed update means a wrong reminder)
* It currently has no understanding of weekends, public holidays, or out-of-office periods, which could affect what "too long" really means

## What next?

This project has a lot of room to grow. Some ideas for the future:

* **Once an application reaches "interview scheduled" stage**, the tool could suggest specific technical and behavioural topics to prepare, tailored to that company and role
* **Pulling commonly asked interview questions** for that specific company or recruiter, to help focus preparation time
* **Personalised learning modules** that only show topics relevant to that particular company's tech stack, instead of generic interview prep
* Turning this into a small web app with reminders sent by email instead of needing to run a script manually
* Connecting it to actual email or LinkedIn data, so the "last contact date" updates automatically instead of being entered by hand

## Acknowledgments

* Built using rule-based logic concepts introduced in the [Building AI](https://buildingai.elementsofai.com/) course by Reaktor and the University of Helsinki
* No external code, datasets, or assets from third parties were used in this project
